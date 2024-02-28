package com.sjaindl.notesdemoapp.domain

import com.sjaindl.notesdemoapp.data.external.Note

interface NoteShareAction {
    fun share(note: Note)
}
