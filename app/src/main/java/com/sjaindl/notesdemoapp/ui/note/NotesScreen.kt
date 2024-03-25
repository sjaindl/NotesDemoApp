package com.sjaindl.notesdemoapp.ui.note

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sjaindl.notesdemoapp.R
import com.sjaindl.notesdemoapp.data.external.Note
import com.sjaindl.notesdemoapp.data.external.ShareType
import com.sjaindl.notesdemoapp.ui.NotesAppBar
import com.sjaindl.notesdemoapp.ui.common.LoadingAnimation
import com.sjaindl.notesdemoapp.ui.theme.NotesDemoAppTheme

@Composable
internal fun NotesScreen(
    modifier: Modifier = Modifier,
    viewModel: NotesViewModel = viewModel(
        factory = NotesViewModel.NotesViewModelFactory(LocalContext.current)
    )
) {
    val notesUIState by viewModel.notesUIState.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current
    val stateFlow = lifecycleOwner.lifecycle.currentStateFlow
    val currentLifecycleState by stateFlow.collectAsState()

    Log.d("Lifecycle_Demo", "current state: $currentLifecycleState")

    if (currentLifecycleState.isAtLeast(Lifecycle.State.STARTED)) {
        viewModel.loadNotes()
    }

    LifecycleEventEffect(Lifecycle.Event.ON_START, lifecycleOwner) {
        Log.d("Lifecycle_Demo", "LifecycleEventEffect ON_START")
    }

    LifecycleStartEffect {
        Log.d("Lifecycle_Demo", "LifecycleStartEffect")

        onStopOrDispose {
            Log.d("Lifecycle_Demo", "LifecycleStartEffect onStopOrDispose")
        }
    }

    NotesScreenContent(
        notesUIState = notesUIState,
        modifier = modifier,
        onAddNote = {
            viewModel.addNote(note = it)
        },
        onDeleteNote = {
            viewModel.deleteNote(note = it)
        },
        onShareNote = {
            viewModel.share(note = it)
        },
        onSync = {
            viewModel.sync()
        },
    )
}

@Composable
internal fun NotesScreenContent(
    notesUIState: NotesUIState,
    modifier: Modifier = Modifier,
    onAddNote: (Note) -> Unit,
    onDeleteNote: (Note) -> Unit,
    onShareNote: (Note) -> Unit,
    onSync: () -> Unit,
) {
    var addNote by remember {
        mutableStateOf(false)
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
            topBar = {
                NotesAppBar(
                    canNavigateBack = false,
                    showSyncAction = true,
                    title = stringResource(R.string.appName),
                    onSync = onSync,
                )
            },
        ) { paddingValues ->

            when (notesUIState) {
                is NotesUIState.Loading -> {
                    Column(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(all = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        LoadingAnimation()
                    }
                }

                is NotesUIState.Error -> {
                    val exception = notesUIState.error

                    val errorMessage = exception.localizedMessage
                        ?: exception.message
                        ?: stringResource(id = R.string.couldNotRetrieveData)

                    Column(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(all = 16.dp),
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = errorMessage,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            fontSize = 20.sp,
                        )
                    }
                }

                is NotesUIState.Content -> {
                    Scaffold(
                        modifier = Modifier
                            .padding(paddingValues),
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
                            items(notesUIState.notes) { note ->
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
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotesScreenContentPreview() {
    NotesDemoAppTheme {
        NotesScreenContent(
            notesUIState = NotesUIState.Content(listOf(
                Note.DatabaseNote(
                    id = 1,
                    creationTime = "2024-01-01T12:00:00",
                    shareType = ShareType.Shareable,
                    title = "DB Note",
                    text = "Test DB note text",
                ),
                Note.FileNote(
                    id = 2,
                    creationTime = "2024-01-02T12:00:00",
                    shareType = ShareType.Unshareable,
                    title = "File Note",
                    text = "Test file note text",
                ),
            )),
            onAddNote = { },
            onDeleteNote = { },
            onShareNote = { },
            onSync = { },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NotesScreenContentErrorPreview() {
    NotesDemoAppTheme {
        NotesScreenContent(
            notesUIState = NotesUIState.Error(Throwable("Notes Error!")),
            onAddNote = { },
            onDeleteNote = { },
            onShareNote = { },
            onSync = { },
        )
    }
}


@Preview(showBackground = true)
@Composable
fun NotesScreenContentLoadingPreview() {
    NotesDemoAppTheme {
        NotesScreenContent(
            notesUIState = NotesUIState.Loading,
            onAddNote = { },
            onDeleteNote = { },
            onShareNote = { },
            onSync = { },
        )
    }
}
