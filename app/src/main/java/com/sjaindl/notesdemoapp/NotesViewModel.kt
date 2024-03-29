package com.sjaindl.notesdemoapp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sjaindl.notesdemoapp.model.Note
import com.sjaindl.notesdemoapp.model.ShareType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotesViewModel(
    private val shareableNotesManager: ShareableNotesManager,
    private val unshareableNotesManager: UnshareableNotesManager,
): ViewModel() {
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes = _notes.asStateFlow()

    fun loadNotes() = viewModelScope.launch {
        _notes.value = shareableNotesManager.load() + unshareableNotesManager.load()
    }

    fun addNote(note: Note) {
        viewModelScope.launch {
            if (note.shareType == ShareType.Shareable) {
                shareableNotesManager.save(note = note)
            } else {
                unshareableNotesManager.save(note = note)
            }
        }
        _notes.value += note
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            if (note.shareType == ShareType.Shareable) {
                shareableNotesManager.delete(note = note)
            } else {
                unshareableNotesManager.delete(note = note)
            }
        }
        _notes.value -= note
    }

    fun share(note: Note) {
        shareableNotesManager.share(note = note)
    }

    class NotesViewModelFactory(
        private val context: Context,
    ) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return NotesViewModel(
                shareableNotesManager = ShareableNotesManager(
                    context = context,
                ),
                unshareableNotesManager = UnshareableNotesManager(
                    context = context,
                ),
            ) as T
        }
    }
}
