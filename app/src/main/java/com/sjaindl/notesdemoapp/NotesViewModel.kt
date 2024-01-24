package com.sjaindl.notesdemoapp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.sjaindl.notesdemoapp.db.AppDatabase
import com.sjaindl.notesdemoapp.model.Note
import com.sjaindl.notesdemoapp.persistence.NotesDatabasePersistence
import com.sjaindl.notesdemoapp.persistence.NotesFilePersistence
import com.sjaindl.notesdemoapp.persistence.NotesPersistence
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotesViewModel(
    private val notesManager: NotesManager,
    private val filePersistence: NotesPersistence,
    private val databasePersistence: NotesPersistence,
): ViewModel() {
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes = _notes.asStateFlow()

    fun loadNotes() = viewModelScope.launch {
        _notes.value = filePersistence.load() + databasePersistence.load()
    }

    fun addNote(note: Note) {
        viewModelScope.launch {
            when(note) {
                is Note.FileNote -> filePersistence.save(note = note)
                is Note.DatabaseNote -> databasePersistence.save(note = note)
            }
        }
        _notes.value += note
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            when(note) {
                is Note.FileNote -> filePersistence.delete(note = note)
                is Note.DatabaseNote -> databasePersistence.delete(note = note)
            }
        }
        _notes.value -= note
    }

    fun share(note: Note) {
        notesManager.share(note = note)
    }

    class NotesViewModelFactory(
        private val context: Context,
    ) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val database by lazy {
                Room.databaseBuilder(
                    context = context,
                    klass = AppDatabase::class.java,
                    name = "database-notes",
                ).build()
            }

            return NotesViewModel(
                notesManager = NotesManager(
                    context = context,
                ),
                filePersistence = NotesFilePersistence(
                    context = context,
                ),
                databasePersistence = NotesDatabasePersistence(
                    database = database,
                ),
            ) as T
        }
    }
}
