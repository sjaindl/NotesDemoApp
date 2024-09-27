package com.sjaindl.notesdemoapp.core.data.file

import kotlinx.serialization.Serializable

@Serializable
data class FileNoteEntity(
    val id: Int,
    val timeStamp: String,
    val shareType: FileShareTypeEntity,
    val title: String,
    val text: String,
)
