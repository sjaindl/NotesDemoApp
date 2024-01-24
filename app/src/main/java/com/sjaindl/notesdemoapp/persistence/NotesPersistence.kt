package com.sjaindl.notesdemoapp.persistence

import com.sjaindl.notesdemoapp.NotePersistenceAction
import com.sjaindl.notesdemoapp.model.Note

interface NotesPersistence {

    suspend fun load(): List<Note>

    suspend fun save(note: Note)

    suspend fun delete(note: Note)
}
