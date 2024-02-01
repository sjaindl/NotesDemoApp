package com.sjaindl.core.domain

sealed interface Note {
    val id: String
    val shareType: ShareType
    val title: String
    val text: String

    data class DatabaseNote(
        override val id: String,
        override val shareType: ShareType,
        override val title: String,
        override val text: String,
    ) : Note

    data class FileNote(
        override val id: String,
        override val shareType: ShareType,
        override val title: String,
        override val text: String,
    ) : Note
}
