package com.sjaindl.notesdemoapp

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.sjaindl.notesdemoapp.model.Note
import com.sjaindl.notesdemoapp.model.ShareType

class NotesManager(
    private val context: Context,
): NoteAction {

    override fun share(note: Note) {
        if (note.shareType == ShareType.Unshareable) return

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "${note.title}\n${note.text}")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        ContextCompat.startActivity(context, shareIntent, null)
    }

    override suspend fun load(): List<Note> {
        // No op
        return emptyList()
    }

    override suspend fun save(note: Note) {
        // No op
    }

    override suspend fun delete(note: Note) {
        // No op
    }
}
