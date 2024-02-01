package com.sjaindl.notesdemoapp.framework.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NotesDao {
    @Query("SELECT * FROM DatabaseNoteEntity")
    suspend fun getAll(): List<DatabaseNoteEntity>

    @Insert
    suspend fun insertAll(vararg notes: DatabaseNoteEntity)

    @Delete
    suspend fun delete(note: DatabaseNoteEntity)
}
