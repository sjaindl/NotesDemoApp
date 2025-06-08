package com.sjaindl.notesdemoapp.data.remote

import com.sjaindl.notesdemoapp.domain.model.Note
import com.sjaindl.notesdemoapp.domain.model.ShareType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

internal class NotesRemoteApi  @Inject constructor(): NotesRemoteDataSource {

    // Simulating network request, returning mocked data
    override suspend fun read(): Flow<List<Note>> {
        delay(timeMillis = 500)

        val notes = listOf(
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
            Note.FileNote(
                id = 4,
                creationTime = "2025-01-02T12:00:00",
                shareType = ShareType.Unshareable,
                title = "Very long File Note",
                text = "This is a very looooooooooooooooong test note text. This is the second line of the very looooooooooooooooong test note text. Lorem ipsum dolor. Lorem ipsum dolor. Lorem ipsum dolor. Lorem ipsum dolor. Lorem ipsum dolor. Lorem ipsum dolor. Lorem ipsum dolor. Lorem ipsum dolor. Lorem ipsum dolor. Lorem ipsum dolor. Lorem ipsum dolor.",
            ),
            Note.FileNote(
                id = 5,
                creationTime = "2024-01-02T12:00:00",
                shareType = ShareType.Unshareable,
                title = "File Note 5",
                text = "Test file note text",
            ),
            Note.FileNote(
                id = 6,
                creationTime = "2024-01-02T12:00:00",
                shareType = ShareType.Unshareable,
                title = "File Note 6",
                text = "Test file note text",
            ),
            Note.FileNote(
                id = 7,
                creationTime = "2024-01-02T12:00:00",
                shareType = ShareType.Unshareable,
                title = "File Note 7",
                text = "Test file note text",
            ),
            Note.FileNote(
                id = 8,
                creationTime = "2024-01-02T12:00:00",
                shareType = ShareType.Unshareable,
                title = "File Note 8",
                text = "Test file note text",
            ),
        )

        return flowOf(value = notes)
    }

    override suspend fun save(note: Note) {
        // Saves note to remote database
    }

    override suspend fun delete(note: Note) {
        // Deletes note from remote database
    }
}
