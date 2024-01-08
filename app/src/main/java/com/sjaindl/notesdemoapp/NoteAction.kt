package com.sjaindl.notesdemoapp

import com.sjaindl.notesdemoapp.model.Note

interface NoteAction {
    fun share(note: Note)
    suspend fun load(): List<Note>
    suspend fun save(note: Note)
    suspend fun delete(note: Note)
}
