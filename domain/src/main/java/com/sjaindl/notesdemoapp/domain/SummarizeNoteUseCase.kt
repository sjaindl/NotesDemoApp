package com.sjaindl.notesdemoapp.domain

import android.content.Context
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.mlkit.genai.common.DownloadCallback
import com.google.mlkit.genai.common.FeatureStatus
import com.google.mlkit.genai.common.GenAiException
import com.google.mlkit.genai.summarization.Summarization
import com.google.mlkit.genai.summarization.SummarizationRequest
import com.google.mlkit.genai.summarization.Summarizer
import com.google.mlkit.genai.summarization.SummarizerOptions
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SummarizeNoteUseCase @Inject constructor() {

    val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    suspend fun summarize(noteText: String, context: Context): String? = withContext(Dispatchers.IO) {

        val deferred = CompletableDeferred<String?>()

        val summarizerOptions = SummarizerOptions.builder(context)
            .setInputType(SummarizerOptions.InputType.ARTICLE)
            .setOutputType(SummarizerOptions.OutputType.ONE_BULLET)
            .setLanguage(SummarizerOptions.Language.ENGLISH)
            .build()

        val summarizer = Summarization.getClient(summarizerOptions)

        prepareAndStartSummarization(
            noteText = noteText,
            summarizer = summarizer,
            deferred = deferred,
        )

        deferred.await()
    }

    private suspend fun prepareAndStartSummarization(
        noteText: String,
        summarizer: Summarizer,
        deferred: CompletableDeferred<String?>,
    ) {
        try {
            // Check feature availability. Status will be one of the following:
            // UNAVAILABLE, DOWNLOADABLE, DOWNLOADING, AVAILABLE
            val featureStatus = summarizer.checkFeatureStatus().get()

            when (featureStatus) {
                FeatureStatus.UNAVAILABLE -> {
                    // fallback to Google Cloud if on device inference is not available
                    val summary = summarizeTextWithGemini(noteText)
                    deferred.complete(summary)
                }
                FeatureStatus.DOWNLOADABLE -> {
                    // Download feature if necessary. If downloadFeature is not called,
                    // the first inference request will also trigger the feature to be
                    // downloaded if it's not already downloaded.
                    summarizer.downloadFeature(
                        object : DownloadCallback {
                            override fun onDownloadStarted(bytesToDownload: Long) { }

                            override fun onDownloadFailed(e: GenAiException) { }

                            override fun onDownloadProgress(totalBytesDownloaded: Long) {}

                            override fun onDownloadCompleted() {
                                scope.launch {
                                    startSummarizationRequest(noteText = noteText, summarizer = summarizer, deferred = deferred)
                                }
                            }
                        }
                    )
                }
                FeatureStatus.DOWNLOADING -> {
                    // Inference request will automatically run once feature is
                    // downloaded. If Gemini Nano is already downloaded on the device,
                    // the feature-specific LoRA adapter model will be downloaded
                    // quickly. However, if Gemini Nano is not already downloaded, the
                    // download process may take longer.
                    startSummarizationRequest(noteText = noteText, summarizer = summarizer, deferred = deferred)
                }
                FeatureStatus.AVAILABLE -> {
                    startSummarizationRequest(noteText = noteText, summarizer = summarizer, deferred = deferred)
                }
            }
        } catch (exception: Exception) {
            deferred.completeExceptionally(exception)
        }
    }

    private suspend fun startSummarizationRequest(
        noteText: String,
        summarizer: Summarizer,
        deferred: CompletableDeferred<String?>,
    ) = withContext(Dispatchers.IO) {
        val summarizationRequest = SummarizationRequest.builder(noteText).build()

        summarizer.runInference(summarizationRequest) { summary ->
            deferred.complete(value = summary)
            summarizer.close()
        }

        //  non-streaming response:
        // val summarizationResult = summarizer.runInference(
        //     summarizationRequest).get().summary
    }

    private suspend fun summarizeTextWithGemini(text: String): String? {
        try {
            val model = GenerativeModel(
                modelName = "gemini-2.5-flash",
                apiKey = BuildConfig.GEMINI_API_KEY,
            )

            val prompt = Content.Builder()
                .text("Summarize the following with one to three bullet points:\n$text")
                .build()

            val response = model.generateContent(prompt)

            val summary = response.text
            return summary

        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}
