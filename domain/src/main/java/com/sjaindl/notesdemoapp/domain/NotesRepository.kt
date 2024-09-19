package com.sjaindl.notesdemoapp.domain

import com.sjaindl.notesdemoapp.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NotesRepository {

    fun read(): Flow<List<Note>>

    suspend fun delete(note: Note)

    suspend fun save(note: Note)

    suspend fun syncFromRemote()
}
