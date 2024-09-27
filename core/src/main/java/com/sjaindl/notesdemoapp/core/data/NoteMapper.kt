package com.sjaindl.notesdemoapp.core.data

import com.sjaindl.notesdemoapp.core.data.db.DatabaseNoteEntity
import com.sjaindl.notesdemoapp.core.data.db.DatabaseShareTypeEntity
import com.sjaindl.notesdemoapp.core.data.file.FileNoteEntity
import com.sjaindl.notesdemoapp.core.data.file.FileShareTypeEntity
import com.sjaindl.notesdemoapp.core.domain.model.Note
import com.sjaindl.notesdemoapp.core.domain.model.ShareType

internal object NoteMapper {

    fun toDatabaseNoteEntity(note: Note): DatabaseNoteEntity {
        return DatabaseNoteEntity(
            id = note.id ?: 0,
            timeStamp = note.creationTime,
            shareType = DatabaseShareTypeEntity.valueOf(note.shareType.name),
            title = note.title,
            text = note.text,
        )
    }

    fun toFileNoteEntity(note: Note): FileNoteEntity {
        return FileNoteEntity(
            id = note.id ?: 0,
            timeStamp = note.creationTime,
            shareType = FileShareTypeEntity.valueOf(note.shareType.name),
            title = note.title,
            text = note.text,
        )
    }

    fun toDatabaseNote(noteEntity: DatabaseNoteEntity): Note.DatabaseNote {
        return Note.DatabaseNote(
            id = noteEntity.id,
            creationTime = noteEntity.timeStamp,
            shareType = ShareType.valueOf(noteEntity.shareType.name),
            title = noteEntity.title,
            text = noteEntity.text,
        )
    }

    fun toFileNote(noteEntity: FileNoteEntity): Note.FileNote {
        return Note.FileNote(
            id = noteEntity.id,
            creationTime = noteEntity.timeStamp,
            shareType = ShareType.valueOf(noteEntity.shareType.name),
            title = noteEntity.title,
            text = noteEntity.text,
        )
    }
}
