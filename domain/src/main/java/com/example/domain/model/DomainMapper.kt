package com.example.domain.model

import com.example.data.dto.PhotoItemDto

internal fun List<PhotoItemDto>.toDomainModel(): List<PhotoItem> {
    val photos = mutableListOf<PhotoItem>()
    this.forEach { photoDto ->
        photos.add(
            with(photoDto) {
                PhotoItem(
                    title = title,
                    url = generatePhotoUrl(
                        farm = farm,
                        server = server,
                        id = id,
                        secret = secret
                    ),
                    isPublic = isPublic,
                    isFriend = isFriend,
                    isFamily = isFamily
                )
            }
        )
    }
    return photos
}

private fun generatePhotoUrl(farm: Int, server: String, id: String, secret: String): String {
    return "https://farm$farm.staticflickr.com/$server/${id}_$secret.jpg"
}