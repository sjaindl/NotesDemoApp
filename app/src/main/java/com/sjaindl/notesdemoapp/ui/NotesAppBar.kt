package com.sjaindl.notesdemoapp.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.sjaindl.notesdemoapp.R
import com.sjaindl.notesdemoapp.ui.theme.NotesDemoAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesAppBar(
    title: String,
    canNavigateBack: Boolean = true,
    showSyncAction: Boolean = false,
    navigateUp: () -> Unit = {},
    onSync: () -> Unit = {},
) {
    NotesDemoAppTheme {
        TopAppBar(
            title = {
                Text(title)
            },
            modifier = Modifier,
            navigationIcon = {
                if (canNavigateBack) {
                    IconButton(onClick = { navigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back),
                        )
                    }
                }
            },
            actions = {
                if (showSyncAction) {
                    IconButton(onClick = { onSync() }) {
                        Icon(
                            imageVector = Icons.Filled.Sync,
                            contentDescription = stringResource(id = R.string.sync),
                        )
                    }
                }
            },
            colors = topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
            )
        )
    }
}

@Preview
@Composable
fun NotesAppBarPreview() {
    NotesAppBar(
        title = stringResource(R.string.appName),
        showSyncAction = true,
        navigateUp = {},
    )
}
