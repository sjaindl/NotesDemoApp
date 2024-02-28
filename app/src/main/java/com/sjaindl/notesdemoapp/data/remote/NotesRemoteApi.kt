package com.sjaindl.notesdemoapp.data.remote

import com.sjaindl.notesdemoapp.data.external.Note
import com.sjaindl.notesdemoapp.data.external.ShareType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class NotesRemoteApi: NotesRemoteDataSource {

    // Simulating network request, returning mocked data
    override suspend fun read(): Flow<List<Note>> {
        delay(timeMillis = 5000)

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
