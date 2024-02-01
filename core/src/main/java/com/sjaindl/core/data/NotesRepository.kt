package com.sjaindl.core.data

import com.sjaindl.core.domain.Note

class NotesRepository(
    private val notesDatabaseDataSource: NotesDataSource,
    private val notesFileDataSource: NotesDataSource,
) {

    suspend fun load(): List<Note> {
        return notesDatabaseDataSource.load() + notesFileDataSource.load()
    }

    suspend fun save(note: Note) {
        when(note) {
            is Note.FileNote -> notesFileDataSource.save(note = note)
            is Note.DatabaseNote -> notesDatabaseDataSource.save(note = note)
        }
    }

    suspend fun delete(note: Note) {
        when(note) {
            is Note.FileNote -> notesFileDataSource.delete(note = note)
            is Note.DatabaseNote -> notesDatabaseDataSource.delete(note = note)
        }
    }
}
