package com.sjaindl.notesdemoapp.core.domain

import com.sjaindl.notesdemoapp.core.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NotesRepository {

    fun read(): Flow<List<Note>>

    suspend fun delete(note: Note)

    suspend fun save(note: Note)
}
