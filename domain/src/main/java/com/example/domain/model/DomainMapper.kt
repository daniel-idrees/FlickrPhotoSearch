package com.example.domain.model

import com.example.data.dto.PhotoItemDto

internal fun List<PhotoItemDto>.toDomainModel(): List<Photo> =
    this.map { photoDto ->
        with(photoDto) {
            Photo(
                title = title,
                url = generatePhotoUrl(),
                isPublic = isPublic.toBoolean("isPublic"),
                isFriend = isFriend.toBoolean("isFriend"),
                isFamily = isFamily.toBoolean("isFamily")
            )
        }
    }

private fun PhotoItemDto.generatePhotoUrl(): String =
    "https://farm$farm.staticflickr.com/$server/${id}_$secret.jpg"

private fun Int.toBoolean(log: String): Boolean =
    when (this) {
        1 -> true
        0 -> false
        else -> throw IllegalArgumentException("$log value must be 0 or 1")
    }
