package com.sjaindl.notesdemoapp.domain

interface SyncNotesRepository {
    suspend fun syncFromRemote()
}
