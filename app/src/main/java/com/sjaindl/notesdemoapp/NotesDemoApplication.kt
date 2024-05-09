package com.sjaindl.notesdemoapp

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.sjaindl.notesdemoapp.model.db.AppDatabase
import com.sjaindl.notesdemoapp.model.persistence.NotesDatabasePersistence
import com.sjaindl.notesdemoapp.model.persistence.NotesFilePersistence
import com.sjaindl.notesdemoapp.model.sharing.NotesManager

class NotesDemoApplication: Application() {
    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()

        appContainer = AppContainer(context = applicationContext)
    }
}

class AppContainer(
    private val context: Context,
) {
    private val database by lazy {
        Room.databaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
            name = "database-notes",
        ).build()
    }

    val notesManager by lazy {
        NotesManager()
    }

    val filePersistence by lazy {
        NotesFilePersistence(
            context = context,
        )
    }

    val databasePersistence by lazy {
        NotesDatabasePersistence(
            database = database,
        )
    }
}
