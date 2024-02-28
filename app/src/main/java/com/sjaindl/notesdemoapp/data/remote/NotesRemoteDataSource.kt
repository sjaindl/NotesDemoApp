package com.sjaindl.notesdemoapp.data.remote

import com.sjaindl.notesdemoapp.data.external.Note
import kotlinx.coroutines.flow.Flow

interface NotesRemoteDataSource {

    suspend fun read(): Flow<List<Note>>

    suspend fun save(note: Note)

    suspend fun delete(note: Note)
}
