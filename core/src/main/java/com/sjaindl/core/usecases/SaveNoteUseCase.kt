package com.sjaindl.core.usecases

import com.sjaindl.core.data.NotesRepository
import com.sjaindl.core.domain.Note

class SaveNoteUseCase(
    private val notesRepository: NotesRepository,
) {

    suspend fun invoke(note: Note) {
       notesRepository.save(note = note)
    }
}
