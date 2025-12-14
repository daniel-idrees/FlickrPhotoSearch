package com.example.data.repository.model

data class Photo(
    val title: String,
    val isPublic: Boolean,
    val isFriend: Boolean,
    val isFamily: Boolean,
    val url: String,
    val owner: String,
)