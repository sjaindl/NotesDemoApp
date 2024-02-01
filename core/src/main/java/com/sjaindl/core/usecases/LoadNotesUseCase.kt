package com.sjaindl.core.usecases

import com.sjaindl.core.data.NotesRepository

class LoadNotesUseCase(
    private val notesRepository: NotesRepository,
) {

    suspend fun invoke() = notesRepository.load()
}
