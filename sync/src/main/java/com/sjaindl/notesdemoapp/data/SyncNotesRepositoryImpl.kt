package com.sjaindl.notesdemoapp.data

import com.sjaindl.notesdemoapp.core.data.di.qualifiers.NotesRemoteDataSourceQualifier
import com.sjaindl.notesdemoapp.core.data.remote.NotesRemoteDataSource
import com.sjaindl.notesdemoapp.core.domain.NotesRepository
import com.sjaindl.notesdemoapp.domain.SyncNotesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SyncNotesRepositoryImpl @Inject constructor(
    private val notesRepository: NotesRepository,
    @NotesRemoteDataSourceQualifier
    private val notesRemoteDataSource: NotesRemoteDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
): SyncNotesRepository {
    override suspend fun syncFromRemote() = withContext(dispatcher) {
        val remoteNotes = notesRemoteDataSource.read().firstOrNull() ?: emptyList()
        val localNotes = notesRepository.read().firstOrNull() ?: emptyList()

        val remoteIds = remoteNotes.map { it.id }
        val toRemove = localNotes.filter {
            (it.id in remoteIds)
        }

        toRemove.forEach {
            notesRepository.delete(note = it)
        }

        remoteNotes.forEach {
            notesRepository.save(note = it)
        }
    }
}
