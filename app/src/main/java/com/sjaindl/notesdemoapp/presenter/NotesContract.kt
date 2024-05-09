package com.sjaindl.notesdemoapp.presenter

import android.content.Context
import com.sjaindl.notesdemoapp.model.Note

interface NotesContract {
    // required: Decouple presenter from Android framework & lifecycle
    interface NotesView {
        fun displayNotes(notes: List<Note>)
    }

    // optional: Decoupling view from concrete presenter
    interface NotesPresenter {
        fun attach(view: NotesView)
        fun detach()
        fun loadNotes()
        fun addNote(note: Note)
        fun deleteNote(note: Note)
        fun share(context: Context, note: Note)
    }
}
