package com.sjaindl.notesdemoapp.controller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.sjaindl.notesdemoapp.model.db.AppDatabase
import com.sjaindl.notesdemoapp.model.Note
import com.sjaindl.notesdemoapp.model.persistence.NotesDatabasePersistence
import com.sjaindl.notesdemoapp.model.persistence.NotesFilePersistence
import com.sjaindl.notesdemoapp.model.sharing.NotesManager
import com.sjaindl.notesdemoapp.view.theme.NotesDemoAppTheme
import com.sjaindl.notesdemoapp.view.NotesScreen
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val database by lazy {
        Room.databaseBuilder(
            context = this,
            klass = AppDatabase::class.java,
            name = "database-notes",
        ).build()
    }

    private val notesManager by lazy {
        NotesManager(
            context = this,
        )
    }

    private val filePersistence by lazy {
        NotesFilePersistence(
            context = this,
        )
    }

    private val databasePersistence by lazy {
        NotesDatabasePersistence(
            database = database,
        )
    }

    private var notes: MutableList<Note> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotesDemoAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NotesScreen(
                        notes = notes,
                        onLoadNotes = ::loadNotes,
                        onAddNote = ::addNote,
                        onDeleteNote = ::deleteNote,
                        onShareNote = ::share,
                    )
                }
            }
        }
    }

    private fun loadNotes() = lifecycleScope.launch {
        notes.apply {
            clear()
            addAll(filePersistence.load())
            addAll(databasePersistence.load())
        }
    }

    private fun addNote(note: Note) {
        lifecycleScope.launch {
            when(note) {
                is Note.FileNote -> filePersistence.save(note = note)
                is Note.DatabaseNote -> databasePersistence.save(note = note)
            }
        }
        notes += note
    }

    private fun deleteNote(note: Note) {
        lifecycleScope.launch {
            when(note) {
                is Note.FileNote -> filePersistence.delete(note = note)
                is Note.DatabaseNote -> databasePersistence.delete(note = note)
            }
        }
        notes -= note
    }

    private fun share(note: Note) {
        notesManager.share(note = note)
    }
}
