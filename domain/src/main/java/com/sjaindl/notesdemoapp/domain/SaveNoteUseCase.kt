package com.sjaindl.notesdemoapp.domain

import com.sjaindl.notesdemoapp.domain.model.Note
import javax.inject.Inject

class SaveNoteUseCase @Inject constructor(
    private val notesRepository: NotesRepository,
) {

    suspend fun invoke(note: Note) {
       notesRepository.save(note = note)
    }
}
