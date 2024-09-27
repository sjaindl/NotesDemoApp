package com.sjaindl.notesdemoapp.core.data.db

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class NotesDatabaseDataSourceImpl @Inject constructor(
    private val database: AppDatabase,
): NotesDatabaseDataSource {

    override fun read(): Flow<List<DatabaseNoteEntity>> {
        return database.notesDao().getAll()
    }

    override suspend fun save(note: DatabaseNoteEntity) {
        saveNote(note = note)
    }

    override suspend fun delete(note: DatabaseNoteEntity) {
        deleteNote(note = note)
    }

    private suspend fun saveNote(note: DatabaseNoteEntity) {
        database.notesDao().insertAll(note)
    }

    private suspend fun deleteNote(note: DatabaseNoteEntity) {
        database.notesDao().delete(note = note)
    }
}
