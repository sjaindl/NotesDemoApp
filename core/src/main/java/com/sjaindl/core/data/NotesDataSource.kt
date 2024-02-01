package com.sjaindl.core.data

import com.sjaindl.core.domain.Note

interface NotesDataSource {

    suspend fun load(): List<Note>

    suspend fun save(note: Note)

    suspend fun delete(note: Note)
}
