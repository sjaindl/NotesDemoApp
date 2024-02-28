package com.sjaindl.notesdemoapp.data

import com.sjaindl.notesdemoapp.data.db.NotesDatabaseDataSource
import com.sjaindl.notesdemoapp.data.external.Note
import com.sjaindl.notesdemoapp.data.file.NotesFileDataSource
import com.sjaindl.notesdemoapp.data.remote.NotesRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class NotesRepository(
    private val notesDatabaseDataSource: NotesDatabaseDataSource,
    private val notesFileDataSource: NotesFileDataSource,
    private val notesRemoteDataSource: NotesRemoteDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {

    fun read(): Flow<List<Note>> {
        return localNotes()
    }

    suspend fun delete(note: Note) = withContext(dispatcher) {
        when(note) {
            is Note.FileNote -> notesFileDataSource.delete(note = NoteMapper.toFileNoteEntity(note = note))
            is Note.DatabaseNote -> notesDatabaseDataSource.delete(note = NoteMapper.toDatabaseNoteEntity(note = note))
        }

        notesRemoteDataSource.delete(note = note)
    }

    suspend fun save(note: Note) = withContext(dispatcher) {
        val nextId = note.id ?: combine(notesDatabaseDataSource.read(), notesFileDataSource.read()) { databaseNotes, fileNotes ->
            val maxDb = databaseNotes.maxOfOrNull { it.id } ?: -1
            val maxFile = fileNotes.maxOfOrNull { it.id } ?: -1

            maxOf(maxDb, maxFile) + 1
        }.first()

        when(note) {
            is Note.FileNote -> {
                notesFileDataSource.save(
                    note = NoteMapper.toFileNoteEntity(note = note.copy(id = nextId))
                )
            }
            is Note.DatabaseNote -> notesDatabaseDataSource.save(
                note = NoteMapper.toDatabaseNoteEntity(note = note.copy(id = nextId))
            )
        }

        notesRemoteDataSource.save(note = note)
    }

    suspend fun sync() = withContext(dispatcher) {
        val remoteNotes = notesRemoteDataSource.read().firstOrNull() ?: emptyList()
        val localNotes = localNotes().firstOrNull() ?: emptyList()

        val remoteIds = remoteNotes.map { it.id }
        val toRemove = localNotes.filter {
            (it.id in remoteIds)
        }

        toRemove.forEach {
            delete(it)
        }

        remoteNotes.forEach {
            save(it)
        }
    }

    private fun localNotes() = combine(notesDatabaseDataSource.read(), notesFileDataSource.read()) { databaseNotes, fileNotes ->
        val externalDatabaseNotes = databaseNotes.map {
            NoteMapper.toDatabaseNote(it)
        }

        val externalFileNotes = fileNotes.map {
            NoteMapper.toFileNote(it)
        }

        (externalDatabaseNotes + externalFileNotes).sortedBy {
            it.id
        }
    }
}
