package com.sjaindl.notesdemoapp.model

import kotlinx.serialization.Serializable
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

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

@OptIn(ExperimentalContracts::class)
fun Note.isFileNote(): Boolean {
    contract {
        returns(true) implies (this@isFileNote is Note.FileNote)
    }
    return this is Note.FileNote
}

@OptIn(ExperimentalContracts::class)
fun Note.isDatabaseNote(): Boolean {
    contract {
        returns(true) implies (this@isDatabaseNote is Note.DatabaseNote)
    }
    return this is Note.DatabaseNote
}
