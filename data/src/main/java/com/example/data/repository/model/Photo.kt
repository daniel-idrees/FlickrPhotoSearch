package com.example.data.repository.model

data class Photo(
    val title: String,
    val photoVisibility: PhotoVisibility,
    val url: String,
    val owner: String,
)


enum class PhotoVisibility {
    PUBLIC,
    FRIEND,
    FAMILY,
    PRIVATE
}