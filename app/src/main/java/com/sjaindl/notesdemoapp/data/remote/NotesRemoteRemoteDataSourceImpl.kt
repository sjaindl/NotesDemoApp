package com.sjaindl.notesdemoapp.data.remote

import com.sjaindl.notesdemoapp.data.external.Note
import kotlinx.coroutines.flow.Flow

internal class NotesRemoteRemoteDataSourceImpl(
    private val api: NotesRemoteApi,
): NotesRemoteDataSource {

    override suspend fun read(): Flow<List<Note>> {
        return api.read()
    }

    override suspend fun save(note: Note) {
        api.save(note = note)
    }

    override suspend fun delete(note: Note) {
        api.delete(note = note)
    }
}
