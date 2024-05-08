package com.sjaindl.notesdemoapp.view

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.sjaindl.notesdemoapp.model.Note
import com.sjaindl.notesdemoapp.model.ShareType
import com.sjaindl.notesdemoapp.view.theme.NotesDemoAppTheme

@Composable
fun NotesScreen(
    notes: List<Note>,
    modifier: Modifier = Modifier,
    onLoadNotes: () -> Unit,
    onAddNote: (Note) -> Unit,
    onDeleteNote: (Note) -> Unit,
    onShareNote: (Note) -> Unit,
) {
    var addNote by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = Unit) {
        onLoadNotes()
    }

    if (addNote) {
        AddNoteScreen(
            modifier = modifier,
            onAddNote = {
                onAddNote(it)
            },
            navigateUp = {
                addNote = false
            }
        )
    } else {
        Scaffold(
            floatingActionButton = {
                IconButton(
                    onClick = {
                        addNote = true
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.inversePrimary,
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
        ) { padding ->
            LazyColumn(
                modifier = Modifier.padding(padding),
            ) {
                items(notes) { note ->
                    SingleNote(
                        note = note,
                        onDelete = {
                            onDeleteNote(note)
                        },
                        onShare = {
                            onShareNote(note)
                        },
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun NotesScreenPreview() {
    NotesDemoAppTheme {
        NotesScreen(
            notes = listOf(
                Note.FileNote(
                    id = "id",
                    shareType = ShareType.Shareable,
                    title = "file note title",
                    text = "text",
                ),
                Note.DatabaseNote(
                    id = "id",
                    shareType = ShareType.Unshareable,
                    title = "database note title",
                    text = "text",
                ),
            ),
            onLoadNotes = {},
            onAddNote = {},
            onDeleteNote = {},
            onShareNote = {},
        )
    }
}
