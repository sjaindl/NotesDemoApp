package com.sjaindl.notesdemoapp.domain

import com.sjaindl.notesdemoapp.data.NotesRepository

class SyncNotesUseCase(
    private val notesRepository: NotesRepository,
) {
    suspend fun invoke() {
        notesRepository.sync()
    }
}
