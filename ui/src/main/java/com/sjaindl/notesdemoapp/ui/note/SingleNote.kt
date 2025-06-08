package com.sjaindl.notesdemoapp.ui.note

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.animateBounds
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.layout.onFirstVisible
import androidx.compose.ui.layout.onLayoutRectChanged
import androidx.compose.ui.layout.onVisibilityChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sjaindl.notesdemoapp.domain.model.Note
import com.sjaindl.notesdemoapp.domain.model.ShareType
import com.sjaindl.notesdemoapp.ui.theme.NotesDemoAppTheme

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SingleNote(
    note: Note,
    modifier: Modifier = Modifier,
    onDelete: () -> Unit,
    onShare: () -> Unit,
) {
    var bgColor by remember {
        mutableStateOf(Color.White)
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 8.dp,
                vertical = 8.dp,
            )
            .onFirstVisible(
                minDurationMs = 1000, // should be at least 1 second visible
            ) {
                println("${note.id} first visible")
            }
            .onLayoutRectChanged(
                throttleMillis = 200,
                debounceMillis = 200,
            ) {
                with(it.boundsInRoot) {
                    println("t: $top, b: $bottom, l: $left, r: $right")
                }
            }
            .onVisibilityChanged { visible ->
                bgColor = if (visible) Color.LightGray else Color.DarkGray
            },
        colors = CardDefaults.cardColors(
            containerColor = bgColor
        )
    ) {

        Box(
            contentAlignment = Alignment.BottomEnd
        ) {
            Column(
                modifier = Modifier
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

                HorizontalDivider()
                LookaheadScope {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = if (expanded) 16.dp else 0.dp)
                            .offset(x = if (expanded) 0.dp else 20.dp)
                            .clickable {
                                expanded = !expanded
                            }
                            .animateBounds(this@LookaheadScope),
                        text = note.text,
                        maxLines = if (expanded) 8 else 3,
                        autoSize = TextAutoSize.StepBased(minFontSize = 10.sp, maxFontSize = 16.sp, stepSize = 2.sp),
                    )
                }

                HorizontalDivider(
                    modifier = Modifier.padding(top = 16.dp)
                )
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
                id = 1,
                creationTime = "2024-01-01T12:00:00",
                shareType = ShareType.Shareable,
                title = "title",
                text = "text",
            ),
            onDelete = { },
            onShare = { },
        )
    }
}
