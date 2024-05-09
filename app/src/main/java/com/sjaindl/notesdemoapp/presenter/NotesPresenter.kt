package com.sjaindl.notesdemoapp.presenter

import android.content.Context
import com.sjaindl.notesdemoapp.AppContainer
import com.sjaindl.notesdemoapp.model.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotesPresenter(
    appContainer: AppContainer,
): NotesContract.NotesPresenter {
    private val notesManager = appContainer.notesManager
    private val filePersistence = appContainer.filePersistence
    private val databasePersistence = appContainer.databasePersistence

    private val coroutineContext = SupervisorJob() + Dispatchers.IO
    private val scope = CoroutineScope(context = coroutineContext)

    private lateinit var view: NotesContract.NotesView

    override fun attach(view: NotesContract.NotesView) {
        this.view = view
    }

    override fun detach() {
        coroutineContext.cancel()
    }

    override fun loadNotes() {
        scope.launch {
            val notes = mutableListOf<Note>().apply {
                addAll(filePersistence.load())
                addAll(databasePersistence.load())
            }

            withContext(Dispatchers.Main) {
                view.displayNotes(notes = notes)
            }
        }
    }

    override fun addNote(note: Note) {
        scope.launch {
            when(note) {
                is Note.FileNote -> filePersistence.save(note = note)
                is Note.DatabaseNote -> databasePersistence.save(note = note)
            }

            withContext(Dispatchers.Main) {
                view.displayNewNote(note = note)
            }
        }
    }

    override fun deleteNote(note: Note) {
        scope.launch {
            when(note) {
                is Note.FileNote -> filePersistence.delete(note = note)
                is Note.DatabaseNote -> databasePersistence.delete(note = note)
            }

            withContext(Dispatchers.Main) {
                view.hideNote(note = note)
            }
        }
    }

    override fun share(context: Context, note: Note) {
        notesManager.share(context = context, note = note)
    }
}
