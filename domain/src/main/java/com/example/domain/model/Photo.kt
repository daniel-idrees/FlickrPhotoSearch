package com.example.domain.model

data class Photo(
    val title: String,
    val url: String,
    val isPublic: Boolean,
    val isFriend: Boolean,
    val isFamily: Boolean,
)
