package com.sjaindl.notesdemoapp.domain

import com.sjaindl.notesdemoapp.data.NotesRepository

class LoadNotesUseCase(
    private val notesRepository: NotesRepository,
) {

    fun invoke() = notesRepository.read()
}
