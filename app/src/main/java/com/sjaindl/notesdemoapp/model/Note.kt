package com.sjaindl.notesdemoapp.model

import kotlinx.serialization.Serializable

@Serializable
sealed interface Note {
    val id: String
    val shareType: ShareType
    val title: String
    val text: String

    @Serializable
    data class DatabaseNote(
        override val id: String,
        override val shareType: ShareType,
        override val title: String,
        override val text: String,
    ) : Note

    @Serializable
    data class FileNote(
        override val id: String,
        override val shareType: ShareType,
        override val title: String,
        override val text: String,
    ) : Note
}
