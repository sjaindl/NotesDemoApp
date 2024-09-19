package com.sjaindl.notesdemoapp.domain

import android.content.Context
import com.sjaindl.notesdemoapp.domain.model.Note

interface NoteShareAction {
    fun share(note: Note, context: Context)
}
