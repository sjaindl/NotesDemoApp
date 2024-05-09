package com.sjaindl.notesdemoapp.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.sjaindl.notesdemoapp.NotesDemoApplication
import com.sjaindl.notesdemoapp.model.Note
import com.sjaindl.notesdemoapp.presenter.NotesContract
import com.sjaindl.notesdemoapp.presenter.NotesPresenter
import com.sjaindl.notesdemoapp.view.theme.NotesDemoAppTheme

class MainActivity : ComponentActivity(), NotesContract.NotesView {

    private val presenter: NotesContract.NotesPresenter by lazy {
        NotesPresenter(
            appContainer = (application as NotesDemoApplication).appContainer,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent(notes = emptyList())
    }

    override fun onStart() {
        super.onStart()

        presenter.attach(view = this)
    }

    override fun onStop() {
        super.onStop()

        presenter.detach()
    }

    private fun setContent(notes: List<Note>) {
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

    private fun loadNotes() {
        presenter.loadNotes()
    }

    private fun addNote(note: Note) {
        presenter.addNote(note = note)
    }

    private fun deleteNote(note: Note) {
        presenter.deleteNote(note = note)
    }

    private fun share(note: Note) {
        presenter.share(context = this, note = note)
    }

    override fun displayNotes(notes: List<Note>) {
        setContent(notes = notes)
    }
}
