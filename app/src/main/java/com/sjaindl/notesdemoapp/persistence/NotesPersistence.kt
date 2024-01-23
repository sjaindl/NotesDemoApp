package com.sjaindl.notesdemoapp.persistence

import com.sjaindl.notesdemoapp.NoteAction
import com.sjaindl.notesdemoapp.model.Note

interface NotesPersistence: NoteAction {

    override fun share(note: Note) {
        // No op
    }

    override suspend fun load(): List<Note>

    override suspend fun save(note: Note)

    override suspend fun delete(note: Note)
}
