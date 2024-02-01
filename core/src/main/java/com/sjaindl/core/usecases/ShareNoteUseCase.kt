package com.sjaindl.core.usecases

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.sjaindl.core.domain.Note
import com.sjaindl.core.domain.ShareType

class ShareNoteUseCase(
    private val context: Context,
): NoteShareAction {

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
}
