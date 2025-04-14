package com.sjaindl.notesdemoapp.model.sharing

import com.sjaindl.notesdemoapp.model.Note

interface NoteShareAction {
    fun share(note: Note)
}
