package com.sjaindl.notesdemoapp.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.accessibility.enableAccessibilityChecks
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sjaindl.notesdemoapp.domain.model.Note
import com.sjaindl.notesdemoapp.domain.model.ShareType
import com.sjaindl.notesdemoapp.ui.note.NotesScreenContent
import com.sjaindl.notesdemoapp.ui.note.NotesUIState
import com.sjaindl.notesdemoapp.ui.theme.NotesDemoAppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NoteScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testNotesScreen() {
        composeTestRule.setContent {
            NotesDemoAppTheme {
                NotesScreenContent(
                    NotesUIState.Content(listOf(
                        Note.DatabaseNote(
                            id = 2,
                            creationTime = "2024-01-01T12:00:00",
                            shareType = ShareType.Shareable,
                            title = "DB Note",
                            text = "Test DB note text",
                        ),
                        Note.FileNote(
                            id = 3,
                            creationTime = "2024-01-02T12:00:00",
                            shareType = ShareType.Unshareable,
                            title = "File Note",
                            text = "Test file note text",
                        ),
                    )),
                    isLoggedIn = true,
                    onAddNote = { },
                    onDeleteNote = { },
                    onShareNote = { },
                    onSync = { },
                    onLoggedIn = { },
                )
            }
        }

        composeTestRule.enableAccessibilityChecks()

        val dbNote = composeTestRule.onNodeWithText("DB Note")
        val fileNote = composeTestRule.onNodeWithText("File Note")
        dbNote.assertIsDisplayed()
        fileNote.assertIsDisplayed()
    }
}