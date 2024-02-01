package com.sjaindl.notesdemoapp.framework.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DatabaseNoteEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao
}
