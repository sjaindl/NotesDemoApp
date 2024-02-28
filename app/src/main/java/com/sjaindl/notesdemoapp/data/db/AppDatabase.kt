package com.sjaindl.notesdemoapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DatabaseNoteEntity::class], version = 1)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao
}
