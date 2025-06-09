package com.sjaindl.notesdemoapp.ui.note

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.sjaindl.notesdemoapp.domain.model.Note
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeableSingleNoteItem(
    note: Note,
    index: Int,
    modifier: Modifier = Modifier,
    onDeleteNote: () -> Unit,
    onShareNote: (Note) -> Unit,
) {
    var showItem by remember {
        mutableStateOf(true)
    }

    val currentOnActualRemove by rememberUpdatedState(onDeleteNote)

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            if (dismissValue == SwipeToDismissBoxValue.StartToEnd || dismissValue == SwipeToDismissBoxValue.EndToStart) {
                if (showItem) {
                    showItem = false
                }
                true
            } else {
                false
            }
        }
    )

    LaunchedEffect(showItem) {
        if (!showItem) {
            // Wait for the AnimatedVisibility exit animation to complete
            delay(350)
            currentOnActualRemove()
        }
    }

    AnimatedVisibility(
        visible = showItem,
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = 300),
            shrinkTowards = Alignment.Top
        ) + fadeOut(animationSpec = tween(durationMillis = 300))
    ) {
        SwipeToDismissBox(
            state = dismissState,
            modifier = modifier
                .semantics(mergeDescendants = true) { },
            enableDismissFromStartToEnd = false,
            enableDismissFromEndToStart = true,
            backgroundContent = {
                val color by animateColorAsState(
                    targetValue = when (dismissState.targetValue) {
                        SwipeToDismissBoxValue.Settled -> Color.Transparent
                        SwipeToDismissBoxValue.EndToStart -> Color.Red.copy(alpha = 0.8f)
                        SwipeToDismissBoxValue.StartToEnd -> Color.Green.copy(alpha = 0.8f)
                    },
                    label = "background color animation"
                )
                val alignment = when (dismissState.dismissDirection) {
                    SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
                    SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
                    else -> Alignment.Center
                }
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(color)
                        .padding(horizontal = 20.dp),
                    contentAlignment = alignment
                ) {
                     if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                         Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
                     }
                }
            }
        ) {
            SingleNote(
                note = note,
                index = index,
                onDelete = {
                    if (showItem) {
                        showItem = false
                    }
                },
                onShare = { onShareNote(note) },
            )
        }
    }
}
