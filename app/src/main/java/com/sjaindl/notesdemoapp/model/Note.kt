package com.sjaindl.notesdemoapp.model

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val id: String,
    val type: NoteType,
    val saveToDatabase: Boolean,
    val title: String,
    val text: String,
)
