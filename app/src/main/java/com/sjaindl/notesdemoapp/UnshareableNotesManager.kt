package com.sjaindl.notesdemoapp

import android.content.Context
import com.sjaindl.notesdemoapp.db.AppDatabase
import com.sjaindl.notesdemoapp.db.NoteEntity
import com.sjaindl.notesdemoapp.model.Note
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class UnshareableNotesManager(
    private val context: Context,
    private val database: AppDatabase,
): NoteAction {

    override fun share(note: Note) {
        // Not supported
    }


    override suspend fun load(): List<Note> {
        return readFromFiles() + readFromDatabase()
    }

    override suspend fun save(note: Note) {
        if (note.saveToDatabase) {
            saveToDatabase(note = note)
        } else {
            saveToFile(note = note)
        }
    }

    override suspend fun delete(note: Note) {
        if (note.saveToDatabase) {
            deleteFromDatabase(note = note)
        } else {
            deleteFromFile(noteId = note.id)
        }
    }

    private fun saveToFile(note: Note) {
        val dir = context.getDir("notes", Context.MODE_PRIVATE)
        val filename = "${note.id}.json"
        val fileContent = Json.encodeToString(note)
        val file = File(dir, filename)

        file.outputStream().use {
            it.write(fileContent.toByteArray())
        }
    }

    private suspend fun saveToDatabase(note: Note) {
        val entity = NoteEntity(
            note.id,
            note.type,
            note.title,
            note.text,
        )

        database.notesDao().insertAll(entity)
    }

    private fun readFromFiles(): List<Note> {
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

    private fun deleteFromFile(noteId: String) {
        val dir = context.getDir("notes", Context.MODE_PRIVATE)
        val filename = "${noteId}.json"
        val file = File(dir, filename)
        file.delete()
    }

    private suspend fun deleteFromDatabase(note: Note) {
        val entity = NoteEntity(
            id = note.id,
            type = note.type,
            title = note.title,
            text = note.text,
        )

        database.notesDao().delete(entity)
    }

    private suspend fun readFromDatabase(): List<Note> {
        return database.notesDao().getAll().map {
            Note(
                id = it.id,
                type = it.type,
                saveToDatabase = true,
                title = it.title,
                text = it.text,
            )
        }
    }
}
