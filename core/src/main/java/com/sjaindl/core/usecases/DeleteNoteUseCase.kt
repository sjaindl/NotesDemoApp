package com.sjaindl.core.usecases

import com.sjaindl.core.data.NotesRepository
import com.sjaindl.core.domain.Note

class DeleteNoteUseCase(
    private val notesRepository: NotesRepository,
) {

    suspend fun invoke(note: Note) {
        notesRepository.delete(note = note)
    }
}
