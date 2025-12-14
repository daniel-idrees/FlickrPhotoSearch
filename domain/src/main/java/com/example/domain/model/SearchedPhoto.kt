package com.example.domain.model


data class SearchedPhoto(
    val title: String,
    val url: String,
    val visibility: Visibility,
)

enum class Visibility {
    PUBLIC,
    FRIEND,
    FAMILY,
    PRIVATE
}
