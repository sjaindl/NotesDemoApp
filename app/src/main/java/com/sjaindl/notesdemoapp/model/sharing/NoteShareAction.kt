package com.sjaindl.notesdemoapp.model.sharing

import android.content.Context
import com.sjaindl.notesdemoapp.model.Note

interface NoteShareAction {
    fun share(context: Context, note: Note)
}
