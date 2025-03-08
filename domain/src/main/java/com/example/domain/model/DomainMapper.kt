package com.example.domain.model

import com.example.data.dto.PhotoItemDto

internal fun List<PhotoItemDto>.toDomainModel(): List<PhotoItem> =
    this.map { photoDto ->
        with(photoDto) {
            PhotoItem(
                title = title,
                url = generatePhotoUrl(),
                isPublic = isPublic,
                isFriend = isFriend,
                isFamily = isFamily
            )
        }
    }

private fun PhotoItemDto.generatePhotoUrl(): String =
    "https://farm$farm.staticflickr.com/$server/${id}_$secret.jpg"
