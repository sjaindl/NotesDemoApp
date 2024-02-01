package com.sjaindl.notesdemoapp.framework.data.file

import com.sjaindl.notesdemoapp.framework.data.ShareTypeEntity
import kotlinx.serialization.Serializable

@Serializable
data class FileNoteEntity(
    val id: String,
    val shareType: ShareTypeEntity,
    val title: String,
    val text: String,
)
