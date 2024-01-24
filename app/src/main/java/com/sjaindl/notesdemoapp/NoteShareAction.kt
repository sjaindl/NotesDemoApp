package com.sjaindl.notesdemoapp

import com.sjaindl.notesdemoapp.model.Note

interface NoteShareAction {
    fun share(note: Note)
}
