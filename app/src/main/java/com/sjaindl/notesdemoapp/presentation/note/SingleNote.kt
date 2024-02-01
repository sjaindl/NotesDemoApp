package com.sjaindl.notesdemoapp.presentation.note

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sjaindl.core.domain.Note
import com.sjaindl.core.domain.ShareType
import com.sjaindl.notesdemoapp.presentation.theme.NotesDemoAppTheme

@Composable
fun SingleNote(
    note: Note,
    modifier: Modifier = Modifier,
    onDelete: () -> Unit,
    onShare: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 8.dp,
                vertical = 8.dp,
            ),
    ) {

        Box(
            contentAlignment = Alignment.BottomEnd
        ) {
            Column(
                modifier = modifier
                    .padding(8.dp)
                    .fillMaxSize()
                    .padding(all = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp),
                    text = note.title,
                )

                Divider()

                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = note.text,
                )
                Divider()
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (note is Note.DatabaseNote) {
                Text(
                    text = "Saved to database",
                    color = Color.Gray,
                    fontSize = 10.sp,
                )
            } else {
                Text(
                    text = "Saved to file system",
                    color = Color.Gray,
                    fontSize = 10.sp,
                )
            }

            if (note.shareType == ShareType.Shareable) {
                IconButton(
                    onClick = onShare,
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                    )
                }
            }

            IconButton(
                onClick = onDelete,
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SingleNotePreview() {
    NotesDemoAppTheme {
        SingleNote(
            note = Note.DatabaseNote(
                id = "1",
                shareType = ShareType.Shareable,
                title = "title",
                text = "text",
            ),
            onDelete = { },
            onShare = { },
        )
    }
}
