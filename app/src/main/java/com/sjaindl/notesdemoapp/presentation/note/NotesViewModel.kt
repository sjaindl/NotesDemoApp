package com.sjaindl.notesdemoapp.presentation.note

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.sjaindl.core.data.NotesRepository
import com.sjaindl.core.domain.Note
import com.sjaindl.core.usecases.DeleteNoteUseCase
import com.sjaindl.core.usecases.LoadNotesUseCase
import com.sjaindl.core.usecases.SaveNoteUseCase
import com.sjaindl.core.usecases.ShareNoteUseCase
import com.sjaindl.notesdemoapp.framework.data.NotesDatabaseDataSource
import com.sjaindl.notesdemoapp.framework.data.NotesFileDataSource
import com.sjaindl.notesdemoapp.framework.db.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotesViewModel(
    private val shareNoteUseCase: ShareNoteUseCase,
    private val loadNotesUseCase: LoadNotesUseCase,
    private val saveNoteUseCase: SaveNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
): ViewModel() {
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes = _notes.asStateFlow()

    fun loadNotes() = viewModelScope.launch {
        _notes.value = loadNotesUseCase.invoke()
    }

    fun addNote(note: Note) {
        viewModelScope.launch {
            saveNoteUseCase.invoke(note = note)
        }
        _notes.value += note
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            deleteNoteUseCase.invoke(note = note)
        }
        _notes.value -= note
    }

    fun share(note: Note) {
        shareNoteUseCase.share(note = note)
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

            val notesFileDataSource = NotesFileDataSource(
                context = context,
            )

            val notesDatabaseDataSource = NotesDatabaseDataSource(
                database = database,
            )

            val notesRepository = NotesRepository(
                notesDatabaseDataSource = notesDatabaseDataSource,
                notesFileDataSource = notesFileDataSource,
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
            ) as T
        }
    }
}
