package com.sjaindl.notesdemoapp.data.file

import kotlinx.serialization.Serializable

@Serializable
data class FileNoteEntity(
    val id: Int,
    val timeStamp: String,
    val shareType: FileShareTypeEntity,
    val title: String,
    val text: String,
)
