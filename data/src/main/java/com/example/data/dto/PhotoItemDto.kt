package com.example.data.dto

data class PhotoItemDto(
    val title: String,
    val isPublic: Int,
    val isFriend: Int,
    val isFamily: Int,
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: Long,
)
