package com.sjaindl.notesdemoapp.data.external

sealed interface Note {
    val id: Int?
    val creationTime: String
    val shareType: ShareType
    val title: String
    val text: String

    data class DatabaseNote(
        override val id: Int?,
        override val creationTime: String,
        override val shareType: ShareType,
        override val title: String,
        override val text: String,
    ) : Note

    data class FileNote(
        override val id: Int?,
        override val creationTime: String,
        override val shareType: ShareType,
        override val title: String,
        override val text: String,
    ) : Note
}
