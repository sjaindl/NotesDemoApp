package com.sjaindl.core.usecases

import com.sjaindl.core.domain.Note

interface NoteShareAction {
    fun share(note: Note)
}
