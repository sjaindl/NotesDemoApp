package com.sjaindl.notesdemoapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sjaindl.notesdemoapp.model.Note
import com.sjaindl.notesdemoapp.model.ShareType
import kotlin.random.Random

@Composable
fun AddNoteScreen(
    modifier: Modifier = Modifier,
    onAddNote: (Note) -> Unit = { },
    navigateUp: () -> Unit = { },
) {
    var title: String? by remember { mutableStateOf(null) }
    var notes: String? by remember { mutableStateOf(null) }
    var shareable by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            NotesAppBar(
                title = stringResource(R.string.addNote),
                navigateUp = navigateUp,
            )
        },
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(all = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1.25f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.Gray,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                ),
                value = title.orEmpty(),
                onValueChange = {
                    title = it
                },
                placeholder = {
                    Text(text = stringResource(id = R.string.addTitle))
                }
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(8f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.Gray,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                ),
                value = notes.orEmpty(),
                onValueChange = {
                    notes = it
                },
                placeholder = {
                    Text(text = stringResource(id = R.string.addNotes))
                }
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Switch(
                    checked = shareable,
                    onCheckedChange = {
                        shareable = it
                    }
                )

                Text(text = stringResource(id = R.string.shareable))
            }

            Divider()

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(all = 8.dp),
                    enabled = !title.isNullOrEmpty() && !notes.isNullOrEmpty(),
                    onClick = {
                        focusManager.clearFocus(force = true)

                        val note = Note.DatabaseNote(
                            id = Random.nextInt(from = 0, until = Int.MAX_VALUE).toString(),
                            shareType = if (shareable) ShareType.Shareable else ShareType.Unshareable,
                            title = title.orEmpty(),
                            text = notes.orEmpty(),
                        )

                        onAddNote(note)
                        navigateUp()
                    },
                ) {
                    Text(
                        text = stringResource(id = R.string.saveNoteDb)
                    )
                }

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(all = 8.dp),
                    enabled = !title.isNullOrEmpty() && !notes.isNullOrEmpty(),
                    onClick = {
                        focusManager.clearFocus(force = true)

                        val note = Note.FileNote(
                            id = Random.nextInt(from = 0, until = Int.MAX_VALUE).toString(),
                            shareType = if (shareable) ShareType.Shareable else ShareType.Unshareable,
                            title = title.orEmpty(),
                            text = notes.orEmpty(),
                        )

                        onAddNote(note)
                        navigateUp()
                    },
                ) {
                    Text(
                        text = stringResource(id = R.string.saveNoteFiles)
                    )
                }
            }

            Button(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                onClick = {
                    focusManager.clearFocus(force = true)
                    navigateUp()
                },
            ) {
                Text(
                    text = stringResource(id = R.string.cancel)
                )
            }
        }
    }
}

@Preview
@Composable
fun AddNoteScreenPreview() {
    AddNoteScreen()
}
