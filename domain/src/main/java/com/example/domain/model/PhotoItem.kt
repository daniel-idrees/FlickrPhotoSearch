package com.example.domain.model

data class PhotoItem(
    val title: String,
    val url: String,
    val isPublic: Boolean,
    val isFriend: Boolean,
    val isFamily: Boolean,
)
