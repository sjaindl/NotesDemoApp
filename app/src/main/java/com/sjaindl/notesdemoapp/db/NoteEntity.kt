package com.sjaindl.notesdemoapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sjaindl.notesdemoapp.model.NoteType

@Entity
data class NoteEntity(
    @PrimaryKey
    val id: String,
    val type: NoteType,
    val title: String,
    val text: String,
)
