package com.sjaindl.notesdemoapp.framework.db

import com.sjaindl.core.data.NotesDataSource
import com.sjaindl.core.domain.Note
import com.sjaindl.core.domain.Note.DatabaseNote
import com.sjaindl.notesdemoapp.framework.NoteMapper

class NotesDatabaseDataSource(
    private val database: AppDatabase,
): NotesDataSource {

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
        val entity = NoteMapper.toDatabaseNoteEntity(note = note)
        database.notesDao().insertAll(entity)
    }

    private suspend fun deleteNote(note: DatabaseNote) {
        val entity = NoteMapper.toDatabaseNoteEntity(note = note)
        database.notesDao().delete(entity)
    }

    private suspend fun read(): List<Note> {
        return database.notesDao().getAll().map {
            NoteMapper.toDatabaseNote(noteEntity = it)
        }
    }
}
