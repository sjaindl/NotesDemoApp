package com.sjaindl.notesdemoapp.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.sjaindl.notesdemoapp.presentation.theme.NotesDemoAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesAppBar(
    title: String,
    navigateUp: () -> Unit = {},
) {
    NotesDemoAppTheme {
        TopAppBar(
            title = {
                Text(title)
            },
            modifier = Modifier,
            navigationIcon = {
                IconButton(onClick = { navigateUp() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back),
                    )
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
        navigateUp = {},
    )
}
