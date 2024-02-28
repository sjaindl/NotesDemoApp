package com.sjaindl.notesdemoapp.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
internal interface NotesDao {
    @Query("SELECT * FROM DatabaseNoteEntity")
    fun getAll(): Flow<List<DatabaseNoteEntity>>

    @Insert
    suspend fun insertAll(vararg notes: DatabaseNoteEntity)

    @Delete
    suspend fun delete(note: DatabaseNoteEntity)
}
