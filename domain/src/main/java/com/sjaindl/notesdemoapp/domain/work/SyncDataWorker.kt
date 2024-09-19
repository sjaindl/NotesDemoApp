package com.sjaindl.notesdemoapp.domain.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.random.Random

class SyncDataWorker(appContext: Context, workerParams: WorkerParameters):
    CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val success = withContext(Dispatchers.IO) {
            syncData()
        }

        return if (success) {
            Result.success()
        } else {
            Result.retry()
        }
    }

    private suspend fun syncData(): Boolean {
        delay(200)
        return Random.nextBoolean()
    }
}
