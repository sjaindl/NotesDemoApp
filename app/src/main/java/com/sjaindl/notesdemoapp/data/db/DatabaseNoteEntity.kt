package com.sjaindl.notesdemoapp.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DatabaseNoteEntity(
    @PrimaryKey
    val id: Int,
    val timeStamp: String,
    val shareType: DatabaseShareTypeEntity,
    val title: String,
    val text: String,
)
