package com.sjaindl.notesdemoapp.framework.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sjaindl.notesdemoapp.framework.data.ShareTypeEntity

@Entity
data class DatabaseNoteEntity(
    @PrimaryKey
    val id: String,
    val type: ShareTypeEntity,
    val title: String,
    val text: String,
)
