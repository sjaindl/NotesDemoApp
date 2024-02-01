package com.sjaindl.notesdemoapp.framework

import com.sjaindl.core.domain.Note
import com.sjaindl.core.domain.ShareType
import com.sjaindl.notesdemoapp.framework.file.FileNoteEntity
import com.sjaindl.notesdemoapp.framework.db.DatabaseNoteEntity

object NoteMapper {
    fun toDatabaseNoteEntity(note: Note): DatabaseNoteEntity {
        return DatabaseNoteEntity(
            id = note.id,
            type = ShareTypeEntity.valueOf(note.shareType.name),
            title = note.title,
            text = note.text,
        )
    }

    fun toFileNoteEntity(note: Note): FileNoteEntity {
        return FileNoteEntity(
            id = note.id,
            shareType = ShareTypeEntity.valueOf(note.shareType.name),
            title = note.title,
            text = note.text,
        )
    }

    fun toDatabaseNote(noteEntity: DatabaseNoteEntity): Note.DatabaseNote {
        return Note.DatabaseNote(
            id = noteEntity.id,
            shareType = ShareType.valueOf(noteEntity.type.name),
            title = noteEntity.title,
            text = noteEntity.text,
        )
    }

    fun toFileNote(noteEntity: FileNoteEntity): Note.FileNote {
        return Note.FileNote(
            id = noteEntity.id,
            shareType = ShareType.valueOf(noteEntity.shareType.name),
            title = noteEntity.title,
            text = noteEntity.text,
        )
    }
}
