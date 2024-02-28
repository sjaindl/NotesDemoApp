package com.sjaindl.notesdemoapp.domain

import com.sjaindl.notesdemoapp.data.NotesRepository
import com.sjaindl.notesdemoapp.data.external.Note

class SaveNoteUseCase(
    private val notesRepository: NotesRepository,
) {

    suspend fun invoke(note: Note) {
       notesRepository.save(note = note)
    }
}
