package com.sjaindl.notesdemoapp.core.data.db

import kotlinx.coroutines.flow.Flow

interface NotesDatabaseDataSource {

    fun read(): Flow<List<DatabaseNoteEntity>>

    suspend fun save(note: DatabaseNoteEntity)

    suspend fun delete(note: DatabaseNoteEntity)
}
