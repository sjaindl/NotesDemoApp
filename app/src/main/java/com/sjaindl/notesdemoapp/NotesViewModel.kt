package com.sjaindl.notesdemoapp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sjaindl.notesdemoapp.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotesViewModel(
    private val notesManager: NotesManager,
    private val notesPersistence: NotesPersistence,
): ViewModel() {
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes = _notes.asStateFlow()

    fun loadNotes() = viewModelScope.launch {
        _notes.value = notesPersistence.load()
    }

    fun addNote(note: Note) {
        viewModelScope.launch {
            notesPersistence.save(note = note)
        }
        _notes.value += note
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            notesPersistence.delete(note = note)
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
            return NotesViewModel(
                notesManager = NotesManager(
                    context = context,
                ),
                notesPersistence = NotesPersistence(
                    context = context,
                )
            ) as T
        }
    }
}
