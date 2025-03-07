package com.example.data.dto.mapper

import com.example.data.dto.PhotoItemDto
import com.example.data.network.model.Photo
import com.example.data.network.model.PhotoSearchResponse

internal fun PhotoSearchResponse.toPhotoList(): List<PhotoItemDto> {
    val photos = mutableListOf<PhotoItemDto>()
    this.photoSearchDetail.photos.forEach { photo ->
        val photoItem = photo.toPhotoItem()
        photos.add(photoItem)
    }

    return photos
}

private fun Photo.toPhotoItem(): PhotoItemDto {
    return PhotoItemDto(
        title = title,
        isPublic = isPublic,
        isFriend = isFriend,
        isFamily = isFamily,
        farm = farm,
        server = server,
        id = id,
        secret = secret,
        owner = owner
    )
}

