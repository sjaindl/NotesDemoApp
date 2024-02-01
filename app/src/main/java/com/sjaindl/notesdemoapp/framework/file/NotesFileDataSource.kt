package com.sjaindl.notesdemoapp.framework.file

import android.content.Context
import com.sjaindl.core.data.NotesDataSource
import com.sjaindl.core.domain.Note
import com.sjaindl.core.domain.Note.FileNote
import com.sjaindl.notesdemoapp.framework.NoteMapper
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class NotesFileDataSource(
    private val context: Context,
): NotesDataSource {

    override suspend fun load(): List<Note> {
        val notes = read()
        return notes.sortedBy {
            it.shareType
        }
    }

    override suspend fun save(note: Note) {
        require(note is FileNote)
        saveNote(note = note)
    }

    override suspend fun delete(note: Note) {
        require(note is FileNote)
        delete(noteId = note.id)
    }

    private fun saveNote(note: FileNote) {
        val fileNoteEntity = NoteMapper.toFileNoteEntity(note = note)
        val dir = context.getDir("notes", Context.MODE_PRIVATE)
        val filename = "${fileNoteEntity.id}.json"
        val fileContent = Json.encodeToString(fileNoteEntity)
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

    private fun read(): List<FileNote> {
        val files = context.getDir("notes", Context.MODE_PRIVATE).listFiles()

        return files?.map { file ->
            val json = file.bufferedReader().useLines { lines ->
                lines.fold("") { some, text ->
                    "$some\n$text"
                }
            }

            val note: FileNoteEntity = Json.decodeFromString(json)
            NoteMapper.toFileNote(note)

        } ?: emptyList()
    }
}
