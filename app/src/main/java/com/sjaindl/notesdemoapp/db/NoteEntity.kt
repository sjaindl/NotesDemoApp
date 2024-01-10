package com.sjaindl.notesdemoapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sjaindl.notesdemoapp.model.ShareType

@Entity
data class NoteEntity(
    @PrimaryKey
    val id: String,
    val type: ShareType,
    val title: String,
    val text: String,
)
