package com.sjaindl.notesdemoapp.persistence

import com.sjaindl.notesdemoapp.db.AppDatabase
import com.sjaindl.notesdemoapp.db.NoteEntity
import com.sjaindl.notesdemoapp.model.Note
import com.sjaindl.notesdemoapp.model.Note.DatabaseNote

class NotesDatabasePersistence(
    private val database: AppDatabase,
): NotesPersistence {

    override suspend fun load(): List<Note> {
        val notes = read()
        return notes.sortedBy {
            it.shareType
        }
    }

    override suspend fun save(note: Note) {
        require(note is DatabaseNote)
        saveNote(note = note)
    }

    override suspend fun delete(note: Note) {
        require(note is DatabaseNote)
        deleteNote(note = note)
    }

    private suspend fun saveNote(note: DatabaseNote) {
        val entity = NoteEntity(
            note.id,
            note.shareType,
            note.title,
            note.text,
        )

        database.notesDao().insertAll(entity)
    }

    private suspend fun deleteNote(note: DatabaseNote) {
        val entity = NoteEntity(
            id = note.id,
            type = note.shareType,
            title = note.title,
            text = note.text,
        )

        database.notesDao().delete(entity)
    }

    private suspend fun read(): List<Note> {
        return database.notesDao().getAll().map {
            DatabaseNote(
                id = it.id,
                shareType = it.type,
                title = it.title,
                text = it.text,
            )
        }
    }
}
