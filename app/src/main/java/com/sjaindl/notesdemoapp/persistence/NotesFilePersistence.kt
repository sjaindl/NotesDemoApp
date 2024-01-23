package com.sjaindl.notesdemoapp.persistence

import android.content.Context
import com.sjaindl.notesdemoapp.model.Note
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class NotesFilePersistence(
    private val context: Context,
): NotesPersistence {

    override suspend fun load(): List<Note> {
        val notes = read()
        return notes.sortedBy {
            it.shareType
        }
    }

    override suspend fun save(note: Note) {
        if (note is Note.FileNote) {
            saveNote(note = note)
        }
    }

    override suspend fun delete(note: Note) {
        if (note is Note.FileNote) {
            delete(noteId = note.id)
        }
    }

    private fun saveNote(note: Note) {
        val fileNote = note as? Note.FileNote ?: return

        val dir = context.getDir("notes", Context.MODE_PRIVATE)
        val filename = "${note.id}.json"
        val fileContent = Json.encodeToString(fileNote)
        val file = File(dir, filename)

        file.outputStream().use {
            it.write(fileContent.toByteArray())
        }
    }

    private fun delete(noteId: String) {
        val dir = context.getDir("notes", Context.MODE_PRIVATE)
        val filename = "${noteId}.json"
        val file = File(dir, filename)
        file.delete()
    }

    private fun read(): List<Note.FileNote> {
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
}
