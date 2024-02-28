package com.sjaindl.notesdemoapp.data.file

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

internal class NotesFileDataSourceImpl(
    private val context: Context,
): NotesFileDataSource {

    private val notesFlow = MutableStateFlow<List<FileNoteEntity>>(emptyList())

    override fun read(): Flow<List<FileNoteEntity>> {
        val notes = readFileNotes()

        notesFlow.value = notes

        return notesFlow
    }

    override suspend fun save(note: FileNoteEntity) {
        saveNote(note = note)
        notesFlow.value = notesFlow.value.toMutableList().apply {
            add(note)
        }
    }

    override suspend fun delete(note: FileNoteEntity) {
        delete(noteId = note.id)
        notesFlow.value = notesFlow.value.toMutableList().apply {
            remove(note)
        }
    }

    private fun saveNote(note: FileNoteEntity) {
        val dir = context.getDir("notes", Context.MODE_PRIVATE)
        val filename = "${note.id}.json"
        val fileContent = Json.encodeToString(note)
        val file = File(dir, filename)

        file.outputStream().use {
            it.write(fileContent.toByteArray())
        }
    }

    private fun delete(noteId: Int) {
        val dir = context.getDir("notes", Context.MODE_PRIVATE)
        val filename = "${noteId}.json"
        val file = File(dir, filename)
        file.delete()
    }

    private fun readFileNotes(): List<FileNoteEntity> {
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
