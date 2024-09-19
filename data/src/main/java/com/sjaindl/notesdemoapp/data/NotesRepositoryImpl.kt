package com.sjaindl.notesdemoapp.data

import com.sjaindl.notesdemoapp.data.db.NotesDatabaseDataSource
import com.sjaindl.notesdemoapp.data.di.qualifiers.NotesRemoteDataSourceQualifier
import com.sjaindl.notesdemoapp.data.file.NotesFileDataSource
import com.sjaindl.notesdemoapp.data.remote.NotesRemoteDataSource
import com.sjaindl.notesdemoapp.domain.NotesRepository
import com.sjaindl.notesdemoapp.domain.model.Note
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotesRepositoryImpl @Inject constructor(
    private val notesDatabaseDataSource: NotesDatabaseDataSource,
    private val notesFileDataSource: NotesFileDataSource,
    @NotesRemoteDataSourceQualifier
    private val notesRemoteDataSource: NotesRemoteDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
): NotesRepository {

    override fun read(): Flow<List<Note>> {
        return localNotes()
    }

    override suspend fun delete(note: Note) = withContext(dispatcher) {
        when(note) {
            is Note.FileNote -> notesFileDataSource.delete(
                note = NoteMapper.toFileNoteEntity(note = note)
            )
            is Note.DatabaseNote -> notesDatabaseDataSource.delete(
                note = NoteMapper.toDatabaseNoteEntity(note = note)
            )
        }

        notesRemoteDataSource.delete(note = note)
    }

    override suspend fun save(note: Note) = withContext(dispatcher) {
        val nextId = note.id ?: combine(notesDatabaseDataSource.read(), notesFileDataSource.read()) { databaseNotes, fileNotes ->
            val maxDb = databaseNotes.maxOfOrNull { it.id } ?: -1
            val maxFile = fileNotes.maxOfOrNull { it.id } ?: -1

            maxOf(maxDb, maxFile) + 1
        }.first()

        when(note) {
            is Note.FileNote -> {
                notesFileDataSource.save(
                    note = NoteMapper.toFileNoteEntity(
                        note = note.copy(
                            id = nextId
                        )
                    )
                )
            }
            is Note.DatabaseNote -> notesDatabaseDataSource.save(
                note = NoteMapper.toDatabaseNoteEntity(
                    note = note.copy(
                        id = nextId
                    )
                )
            )
        }

        notesRemoteDataSource.save(note = note)
    }

    override suspend fun syncFromRemote() = withContext(dispatcher) {
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
