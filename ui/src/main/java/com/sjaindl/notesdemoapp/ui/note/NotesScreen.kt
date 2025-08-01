package com.sjaindl.notesdemoapp.ui.note

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CollectionInfo
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.collectionInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sjaindl.notesdemoapp.domain.model.Note
import com.sjaindl.notesdemoapp.domain.model.ShareType
import com.sjaindl.notesdemoapp.ui.NotesAppBar
import com.sjaindl.notesdemoapp.ui.R
import com.sjaindl.notesdemoapp.ui.common.LoadingAnimation
import com.sjaindl.notesdemoapp.ui.theme.NotesDemoAppTheme
import kotlinx.coroutines.launch

@Composable
fun NotesScreen(
    modifier: Modifier = Modifier,
) {
    NotesScreenContent(
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NotesScreenContent(
    modifier: Modifier = Modifier,
    viewModel: NotesViewModel = viewModel(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val stateFlow = lifecycleOwner.lifecycle.currentStateFlow
    val currentLifecycleState by stateFlow.collectAsState()
    val context = LocalContext.current

    val notesUIState by viewModel.notesUIState.collectAsStateWithLifecycle(
        lifecycleOwner = lifecycleOwner,
        minActiveState = Lifecycle.State.STARTED,
    )

    val summary by viewModel.summary.collectAsStateWithLifecycle()

    // collectAsStateWithLifecycle is shorthand for:
    /*
    produceState<NotesUIState>(initialValue = NotesUIState.Loading, key1 = lifecycleOwner.lifecycle, key2 = viewModel) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.notesUIState.collect {
                val notesUIState = it
            }
        }
    }
     */

    Log.d("Lifecycle_Demo", "current state: $currentLifecycleState")

    LaunchedEffect(Unit) {
        viewModel.loadNotes()
    }

    LifecycleEventEffect(Lifecycle.Event.ON_START, lifecycleOwner) {
        Log.d("Lifecycle_Demo", "LifecycleEventEffect ON_START")
    }

    LifecycleStartEffect(Unit) {
        Log.d("Lifecycle_Demo", "LifecycleStartEffect")

        onStopOrDispose {
            Log.d("Lifecycle_Demo", "LifecycleStartEffect onStopOrDispose")
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sync() // fill some test notes in the beginning
    }

    summary?.let {
        BasicAlertDialog(
            onDismissRequest = {
                viewModel.resetSummary()
            },
            modifier = Modifier
                .background(color = Color.Green)
                .verticalScroll(rememberScrollState()),
            properties = DialogProperties(windowTitle = "Note summary"),
        ) {
            Text(text = it)
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
            viewModel.share(note = it, context = context)
        },
        onSummarize = {
            viewModel.summarize(it, context)
        },
        onProofread = {
            // TODO
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
    onSummarize: (Note) -> Unit,
    onProofread: (Note) -> Unit,
    onSync: () -> Unit,
) {
    var addNote by remember {
        mutableStateOf(false)
    }

    val snackBarHostState = remember {
        SnackbarHostState()
    }

    val scope = rememberCoroutineScope()

    if (addNote) {
        AddNoteScreen(
            modifier = modifier,
            onAddNote = {
                onAddNote(it)

                scope.launch {
                    snackBarHostState.showSnackbar("Note added")
                }
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
                    title = stringResource(R.string.appName),
                    onSync = onSync,
                )
            },
            snackbarHost = {
                SnackbarHost(
                    hostState = snackBarHostState,
                    snackbar = { snackBarData ->
                        Snackbar(
                            modifier = Modifier.semantics {
                                contentDescription = snackBarData.visuals.message
                                liveRegion = LiveRegionMode.Polite
                            }
                        ) {
                            Text(snackBarData.visuals.message)
                        }
                    }
                )
            }
        ) { paddingValues ->

            when (notesUIState) {
                is NotesUIState.Loading -> {
                    Column(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(horizontal = 16.dp)
                            .semantics {
                                progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate
                            },
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
                            modifier = Modifier
                                .padding(padding)
                                .semantics {
                                    collectionInfo = CollectionInfo(
                                        rowCount = notesUIState.notes.size,
                                        columnCount = 1,
                                    )
                                },
                        ) {
                            itemsIndexed(
                                items = notesUIState.notes,
                                key = { index, note ->
                                    note.id!!
                                },
                            ) { index, note ->

                                SwipeableSingleNoteItem(
                                    note = note,
                                    index = index,
                                    modifier = Modifier.semantics {
                                        customActions = listOf(
                                            CustomAccessibilityAction(
                                                label = "Swipe to dismiss",
                                                action = {
                                                    onDeleteNote
                                                    true
                                                },
                                            )
                                        )
                                    },
                                    onDeleteNote = {
                                        onDeleteNote(note)
                                    },
                                    onShareNote = onShareNote,
                                    onSummarize = onSummarize,
                                    onProofread = onProofread,
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
            onSummarize = { },
            onProofread = { },
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
            onSummarize = { },
            onProofread = { },
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
            onSummarize = { },
            onProofread = { },
            onSync = { },
        )
    }
}
