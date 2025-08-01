package com.sjaindl.notesdemoapp.ui.note

import android.content.ClipData
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
import androidx.compose.material.icons.filled.Summarize
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
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sjaindl.notesdemoapp.domain.model.Note
import com.sjaindl.notesdemoapp.domain.model.ShareType
import com.sjaindl.notesdemoapp.ui.common.NoteIndex
import com.sjaindl.notesdemoapp.ui.theme.NotesDemoAppTheme

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SingleNote(
    note: Note,
    index: Int,
    modifier: Modifier = Modifier,
    onDelete: () -> Unit,
    onShare: () -> Unit,
    onSummarize: () -> Unit,
    onProofread: () -> Unit,
) {
    val clipboard = LocalClipboard.current

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
            .clickable(
                onClickLabel = "Copy note to clipboard",
                onClick = {
                    val clipData = ClipData.newPlainText("Copied ${note.title}", note.text)
                    clipboard.nativeClipboard.setPrimaryClip(clipData)
                }
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
                //bgColor = if (visible) Color.LightGray else Color.DarkGray
            }
            .semantics {
                isTraversalGroup = true
            },
        colors = CardDefaults.cardColors(
            containerColor = bgColor
        )
    ) {

        Box(
            contentAlignment = Alignment.BottomEnd,
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
                        .height(32.dp)
                        .semantics {
                            traversalIndex = 2f
                            heading()
                        },
                    text = note.title,
                )

                HorizontalDivider()

                LookaheadScope {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        NoteIndex(
                            number = index,
                            modifier = Modifier
                                .semantics {
                                    traversalIndex = 1f
                                    contentDescription = "Note number ${index + 1}"
                                },
                        )

                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = if (expanded) 24.dp else 8.dp)
                                .offset(x = if (expanded) 8.dp else 24.dp)
                                .clickable {
                                    expanded = !expanded
                                }
                                .animateBounds(this@LookaheadScope)
                                .semantics {
                                    traversalIndex = 3f
                                },
                            text = note.text,
                            maxLines = if (expanded) 60 else 2,
                            autoSize = TextAutoSize.StepBased(minFontSize = 10.sp, maxFontSize = 16.sp, stepSize = 2.sp),
                        )
                    }
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
                    modifier = Modifier
                        .semantics {
                            traversalIndex = 4f
                        },
                    color = Color.Gray,
                    fontSize = 10.sp,
                )
            } else {
                Text(
                    text = "Saved to file system",
                    modifier = Modifier
                        .semantics {
                            traversalIndex = 4f
                        },
                    color = Color.Gray,
                    fontSize = 10.sp,
                )
            }

            if (note.shareType == ShareType.Shareable) {
                IconButton(
                    modifier = Modifier
                        .semantics {
                            traversalIndex = 5f
                        },
                    onClick = onShare,
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                    )
                }
            }

            IconButton(
                modifier = Modifier
                    .semantics {
                        traversalIndex = 6f
                    },
                onClick = onDelete,
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                )
            }

            IconButton(
                modifier = Modifier
                    .semantics {
                        traversalIndex = 7f
                    },
                onClick = onSummarize,
            ) {
                Icon(
                    imageVector = Icons.Default.Summarize,
                    contentDescription = "Summarize note",
                )
            }

            /*
            TODO: Proofreading
            IconButton(
                modifier = Modifier
                    .semantics {
                        traversalIndex = 8f
                    },
                onClick = onProofread,
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Proofread note",
                )
            }

             */
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SingleNotePreview() {
    NotesDemoAppTheme {
        SingleNote(
            index = 1,
            note = Note.DatabaseNote(
                id = 1,
                creationTime = "2024-01-01T12:00:00",
                shareType = ShareType.Shareable,
                title = "title",
                text = "text",
            ),
            onDelete = { },
            onShare = { },
            onSummarize = { },
            onProofread = { },
        )
    }
}
