package com.example.domain.model

data class PhotoItem(
    val title: String,
    val url: String,
    val isPublic: Int,
    val isFriend: Int,
    val isFamily: Int,
)
