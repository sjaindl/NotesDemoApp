package com.sjaindl.notesdemoapp.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NotesDao {
    @Query("SELECT * FROM NoteEntity")
    suspend fun getAll(): List<NoteEntity>

    @Insert
    suspend fun insertAll(vararg notes: NoteEntity)

    @Delete
    suspend fun delete(note: NoteEntity)
}
