package com.sjaindl.notesdemoapp.core.data.remote

import com.sjaindl.notesdemoapp.core.data.di.qualifiers.NotesRemoteApiDataSource
import com.sjaindl.notesdemoapp.core.domain.model.Note
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class NotesRemoteRemoteDataSourceImpl @Inject constructor(
    @NotesRemoteApiDataSource
    private val api: NotesRemoteDataSource,
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
