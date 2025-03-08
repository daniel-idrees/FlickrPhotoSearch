package com.example.data.dto.mapper

import com.example.data.dto.PhotoItemDto
import com.example.data.network.model.Photo
import com.example.data.network.model.PhotoSearchResponse

internal fun PhotoSearchResponse.toPhotoList(): List<PhotoItemDto> =
    this.photoSearchDetail.photos.map { photo ->
        photo.toPhotoItem()
    }


private fun Photo.toPhotoItem(): PhotoItemDto =
    PhotoItemDto(
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
