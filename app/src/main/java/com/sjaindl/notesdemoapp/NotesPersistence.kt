package com.sjaindl.notesdemoapp

import android.content.Context
import androidx.room.Room
import com.sjaindl.notesdemoapp.db.AppDatabase
import com.sjaindl.notesdemoapp.db.NoteEntity
import com.sjaindl.notesdemoapp.model.Note
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class NotesPersistence(
    private val context: Context,
): NoteAction {

    private val database by lazy {
        Room.databaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
            name = "database-notes",
        ).build()
    }

    override fun share(note: Note) {
        // No Op
    }

    override suspend fun load(): List<Note> {
        val notes = readFromFiles() + readFromDatabase()
        return notes.sortedBy {
            it.shareType
        }
    }

    override suspend fun save(note: Note) {
        if (note is Note.DatabaseNote) {
            saveToDatabase(note = note)
        } else {
            saveToFile(note = note)
        }
    }

    override suspend fun delete(note: Note) {
        if (note is Note.DatabaseNote) {
            deleteFromDatabase(note = note)
        } else {
            deleteFromFile(noteId = note.id)
        }
    }

    private fun saveToFile(note: Note) {
        val fileNote = note as? Note.FileNote ?: return

        val dir = context.getDir("notes", Context.MODE_PRIVATE)
        val filename = "${note.id}.json"
        val fileContent = Json.encodeToString(fileNote)
        val file = File(dir, filename)

        file.outputStream().use {
            it.write(fileContent.toByteArray())
        }
    }

    private suspend fun saveToDatabase(note: Note) {
        val entity = NoteEntity(
            note.id,
            note.shareType,
            note.title,
            note.text,
        )

        database.notesDao().insertAll(entity)
    }

    private fun deleteFromFile(noteId: String) {
        val dir = context.getDir("notes", Context.MODE_PRIVATE)
        val filename = "${noteId}.json"
        val file = File(dir, filename)
        file.delete()
    }

    private suspend fun deleteFromDatabase(note: Note) {
        val entity = NoteEntity(
            id = note.id,
            type = note.shareType,
            title = note.title,
            text = note.text,
        )

        database.notesDao().delete(entity)
    }

    private fun readFromFiles(): List<Note.FileNote> {
        val files = context.getDir("notes", Context.MODE_PRIVATE).listFiles()

        return files?.map { file ->
            val json = file.bufferedReader().useLines { lines ->
                lines.fold("") { some, text ->
                    "$some\n$text"
                }
            }

            Json.decodeFromString(json)
        } ?: emptyList()
    }

    private suspend fun readFromDatabase(): List<Note> {
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
