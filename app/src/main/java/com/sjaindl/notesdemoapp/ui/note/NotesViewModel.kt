package com.sjaindl.notesdemoapp.ui.note

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.room.Room
import com.sjaindl.notesdemoapp.data.NotesRepository
import com.sjaindl.notesdemoapp.data.db.AppDatabase
import com.sjaindl.notesdemoapp.data.db.NotesDatabaseDataSourceImpl
import com.sjaindl.notesdemoapp.data.external.Note
import com.sjaindl.notesdemoapp.data.file.NotesFileDataSourceImpl
import com.sjaindl.notesdemoapp.data.remote.NotesRemoteApi
import com.sjaindl.notesdemoapp.data.remote.NotesRemoteRemoteDataSourceImpl
import com.sjaindl.notesdemoapp.domain.DeleteNoteUseCase
import com.sjaindl.notesdemoapp.domain.LoadNotesUseCase
import com.sjaindl.notesdemoapp.domain.SaveNoteUseCase
import com.sjaindl.notesdemoapp.domain.ShareNoteUseCase
import com.sjaindl.notesdemoapp.domain.SyncNotesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal sealed class NotesUIState {
    data object Loading: NotesUIState()

    data class Error(val error: Throwable): NotesUIState()

    data class Content(val notes: List<Note>): NotesUIState()
}

internal class NotesViewModel(
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
        val flow = savedStateHandle.getStateFlow("notes") {
            emptyList<Note>()
        }

        if(flow.value().isNotEmpty()) {
            _notesUIState.value = NotesUIState.Content(notes = flow.value())
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

    fun share(note: Note) {
        shareNoteUseCase.share(note = note)
    }

    class NotesViewModelFactory() : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            val context = extras[VIEW_MODEL_STORE_OWNER_KEY] as Context

            val database by lazy {
                Room.databaseBuilder(
                    context = context,
                    klass = AppDatabase::class.java,
                    name = "database-notes",
                ).build()
            }

            val notesFileDataSource = NotesFileDataSourceImpl(
                context = context,
            )

            val notesDatabaseDataSource = NotesDatabaseDataSourceImpl(
                database = database,
            )

            val notesRemoteDataSource = NotesRemoteRemoteDataSourceImpl(
                api = NotesRemoteApi()
            )

            val notesRepository = NotesRepository(
                notesDatabaseDataSource = notesDatabaseDataSource,
                notesFileDataSource = notesFileDataSource,
                notesRemoteDataSource = notesRemoteDataSource,
            )

            return NotesViewModel(
                shareNoteUseCase = ShareNoteUseCase(
                    context = context,
                ),
                loadNotesUseCase = LoadNotesUseCase(
                        notesRepository = notesRepository,
                ),
                saveNoteUseCase = SaveNoteUseCase(
                    notesRepository = notesRepository,
                ),
                deleteNoteUseCase = DeleteNoteUseCase(
                    notesRepository = notesRepository,
                ),
                syncNotesUseCase = SyncNotesUseCase(
                    notesRepository = notesRepository,
                ),
                savedStateHandle = extras.createSavedStateHandle()
            ) as T
        }
    }
}
