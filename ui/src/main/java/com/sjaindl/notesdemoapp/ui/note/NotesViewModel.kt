package com.sjaindl.notesdemoapp.ui.note

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjaindl.notesdemoapp.domain.DeleteNoteUseCase
import com.sjaindl.notesdemoapp.domain.LoadNotesUseCase
import com.sjaindl.notesdemoapp.domain.SaveNoteUseCase
import com.sjaindl.notesdemoapp.domain.ShareNoteUseCase
import com.sjaindl.notesdemoapp.domain.SyncNotesUseCase
import com.sjaindl.notesdemoapp.domain.model.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

internal sealed class NotesUIState {
    data object Loading: NotesUIState()

    data class Error(val error: Throwable): NotesUIState()

    data class Content(val notes: List<Note>): NotesUIState()
}

@HiltViewModel
internal class NotesViewModel @Inject constructor(
    private val shareNoteUseCase: ShareNoteUseCase,
    private val loadNotesUseCase: LoadNotesUseCase,
    private val saveNoteUseCase: SaveNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val syncNotesUseCase: SyncNotesUseCase,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val _notesUIState = MutableStateFlow<NotesUIState>(NotesUIState.Loading)

    val notesUIState = _notesUIState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        initialValue = NotesUIState.Loading,
    )

    fun loadNotes() = viewModelScope.launch {
        val notesFlow: StateFlow<List<Note>> = savedStateHandle.getStateFlow(key = "notes", emptyList())

        if(notesFlow.value.isNotEmpty()) {
            _notesUIState.value = NotesUIState.Content(notes = notesFlow.value)
        } else {
            loadNotesUseCase.invoke()
                .catch {
                    _notesUIState.value = NotesUIState.Error(it)
                }
                .collectLatest { notes ->
                    savedStateHandle["notes"] = notes
                    _notesUIState.value = NotesUIState.Content(notes = notes)
                }
        }
    }

    fun addNote(note: Note) {
        viewModelScope.launch {
            saveNoteUseCase.invoke(note = note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            deleteNoteUseCase.invoke(note = note)
        }
    }

    fun sync() = viewModelScope.launch {
        _notesUIState.value = NotesUIState.Loading
        syncNotesUseCase.invoke()
    }

    fun share(note: Note, context: Context) {
        shareNoteUseCase.share(note = note, context = context)
    }
}
