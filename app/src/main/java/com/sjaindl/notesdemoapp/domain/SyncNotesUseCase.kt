package com.sjaindl.notesdemoapp.domain

import android.util.Log
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.WorkRequest.Companion.MIN_BACKOFF_MILLIS
import com.sjaindl.notesdemoapp.data.NotesRepository
import com.sjaindl.notesdemoapp.data.work.SyncDataWorker
import kotlinx.coroutines.flow.collectLatest
import java.util.concurrent.TimeUnit

class SyncNotesUseCase(
    private val notesRepository: NotesRepository,
    private val workManager: WorkManager,
) {
    suspend fun invoke() {
        notesRepository.syncFromRemote()
        syncToRemote()
    }

    private suspend fun syncToRemote() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()

        val syncDataWorkRequest =
            OneTimeWorkRequestBuilder<SyncDataWorker>()
                .setConstraints(constraints)
                .setExpedited(policy = OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setBackoffCriteria(
                    backoffPolicy = BackoffPolicy.LINEAR,
                    backoffDelay = MIN_BACKOFF_MILLIS, // 10 seconds
                    timeUnit = TimeUnit.MILLISECONDS,
                )
                .addTag("sync")
                .build()


        // Enqueue regular work:
        // workManager.enqueue(syncDataWorkRequest)

        // Enqueue unique work:
        workManager.enqueueUniqueWork(
            "oneTimeSync",
            ExistingWorkPolicy.KEEP,
            syncDataWorkRequest,
        )

        // by id
        workManager.getWorkInfoByIdFlow(syncDataWorkRequest.id).collectLatest { workInfo ->
            Log.d("WM-Status", "WorkInfo by ID: $workInfo")
        }

        /*
        Alternative observing via tag or unique name:

        // by tag
        workManager.getWorkInfosByTagFlow("sync").collectLatest { workInfoList ->
            Log.d("WM-Status", "WorkInfo by tag: $workInfoList")
        }

        // by name
        workManager.getWorkInfosForUniqueWorkFlow("oneTimeSync").collectLatest { workInfoList ->
            Log.d("WM-Status", "WorkInfo by name: $workInfoList")
        }
         */
    }
}
