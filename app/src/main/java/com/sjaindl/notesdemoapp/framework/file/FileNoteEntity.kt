package com.sjaindl.notesdemoapp.framework.file

import com.sjaindl.notesdemoapp.framework.ShareTypeEntity
import kotlinx.serialization.Serializable

@Serializable
data class FileNoteEntity(
    val id: String,
    val shareType: ShareTypeEntity,
    val title: String,
    val text: String,
)
