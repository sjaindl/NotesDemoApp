package com.sjaindl.notesdemoapp.presentation.note

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sjaindl.notesdemoapp.presentation.theme.NotesDemoAppTheme

@Composable
fun NotesScreen(
    modifier: Modifier = Modifier,
    viewModel: NotesViewModel = viewModel(
        factory = NotesViewModel.NotesViewModelFactory(LocalContext.current)
    )
) {
    var addNote by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.loadNotes()
    }

    val notes = viewModel.notes.collectAsState()

    if (addNote) {
        AddNoteScreen(
            modifier = modifier,
            onAddNote = {
                viewModel.addNote(note = it)
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
                items(notes.value) { note ->
                    SingleNote(
                        note = note,
                        onDelete = {
                           viewModel.deleteNote(note = note)
                        },
                        onShare = {
                            viewModel.share(note = note)
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
        NotesScreen()
    }
}
