package com.sjaindl.notesdemoapp.core.data

import com.sjaindl.notesdemoapp.core.data.db.NotesDatabaseDataSource
import com.sjaindl.notesdemoapp.core.data.di.qualifiers.NotesRemoteDataSourceQualifier
import com.sjaindl.notesdemoapp.core.data.file.NotesFileDataSource
import com.sjaindl.notesdemoapp.core.data.remote.NotesRemoteDataSource
import com.sjaindl.notesdemoapp.core.domain.NotesRepository
import com.sjaindl.notesdemoapp.core.domain.model.Note
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
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
