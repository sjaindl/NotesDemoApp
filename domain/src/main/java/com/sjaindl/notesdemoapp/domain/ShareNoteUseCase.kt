package com.sjaindl.notesdemoapp.domain

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.sjaindl.notesdemoapp.domain.model.Note
import com.sjaindl.notesdemoapp.domain.model.ShareType
import javax.inject.Inject

class ShareNoteUseCase @Inject constructor(): NoteShareAction {

    override fun share(note: Note, context: Context) {
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
