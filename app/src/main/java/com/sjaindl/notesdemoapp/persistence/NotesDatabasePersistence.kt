package com.sjaindl.notesdemoapp.persistence

import android.content.Context
import androidx.room.Room
import com.sjaindl.notesdemoapp.db.AppDatabase
import com.sjaindl.notesdemoapp.db.NoteEntity
import com.sjaindl.notesdemoapp.model.Note

class NotesDatabasePersistence(
    private val context: Context,
): NotesPersistence {

    private val database by lazy {
        Room.databaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
            name = "database-notes",
        ).build()
    }

    override suspend fun load(): List<Note> {
        val notes = read()
        return notes.sortedBy {
            it.shareType
        }
    }

    override suspend fun save(note: Note) {
        if (note is Note.DatabaseNote) {
            saveNote(note = note)
        }
    }

    override suspend fun delete(note: Note) {
        if (note is Note.DatabaseNote) {
            deleteNote(note = note)
        }
    }

    private suspend fun saveNote(note: Note) {
        val entity = NoteEntity(
            note.id,
            note.shareType,
            note.title,
            note.text,
        )

        database.notesDao().insertAll(entity)
    }

    private suspend fun deleteNote(note: Note) {
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
            Note.DatabaseNote(
                id = it.id,
                shareType = it.type,
                title = it.title,
                text = it.text,
            )
        }
    }
}
