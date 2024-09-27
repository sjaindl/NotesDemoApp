package com.sjaindl.notesdemoapp.core.data.file

import kotlinx.coroutines.flow.Flow

interface NotesFileDataSource {

    fun read(): Flow<List<FileNoteEntity>>

    suspend fun save(note: FileNoteEntity)

    suspend fun delete(note: FileNoteEntity)
}
