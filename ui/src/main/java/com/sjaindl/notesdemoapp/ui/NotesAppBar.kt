package com.sjaindl.notesdemoapp.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Login
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
import com.sjaindl.notesdemoapp.ui.theme.NotesDemoAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesAppBar(
    title: String,
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = true,
    showSyncAction: Boolean = false,
    showLogin: Boolean = false,
    navigateUp: () -> Unit = {},
    onSync: () -> Unit = {},
    onLogin: () -> Unit = {},
) {
    NotesDemoAppTheme {
        TopAppBar(
            title = {
                Text(title)
            },
            modifier = modifier,
            navigationIcon = {
                if (canNavigateBack) {
                    IconButton(onClick = { navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back),
                        )
                    }
                }
            },
            actions = {
                if (showLogin) {
                    IconButton(onClick = { onLogin() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Login,
                            contentDescription = stringResource(id = R.string.sync),
                        )
                    }
                }
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
                containerColor = MaterialTheme.colorScheme.primary.copy(
                    alpha = 0.4f
                ),
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
