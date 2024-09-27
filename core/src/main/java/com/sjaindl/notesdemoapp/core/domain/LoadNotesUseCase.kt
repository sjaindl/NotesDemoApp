package com.sjaindl.notesdemoapp.core.domain

import javax.inject.Inject

class LoadNotesUseCase @Inject constructor(
    private val notesRepository: NotesRepository,
) {

    fun invoke() = notesRepository.read()
}
