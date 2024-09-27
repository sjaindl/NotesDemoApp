package com.sjaindl.notesdemoapp.core.domain

import com.sjaindl.notesdemoapp.core.domain.model.Note
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val notesRepository: NotesRepository,
) {

    suspend fun invoke(note: Note) {
        notesRepository.delete(note = note)
    }
}
