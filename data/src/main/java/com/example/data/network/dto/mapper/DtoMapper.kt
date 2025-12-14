package com.example.data.network.dto.mapper

import com.example.data.repository.model.Photo
import com.example.data.network.dto.PhotoDto
import com.example.data.network.dto.PhotoSearchResponseDto

internal fun PhotoSearchResponseDto.toPhotoList(): List<Photo> =
    this.photoSearchDetailDto.photosDto.map { photo ->
        photo.toExternalModel()
    }


private fun PhotoDto.toExternalModel(): Photo =
    Photo(
        title = title,
        url = generatePhotoUrl(),
        isPublic = isPublic.toBoolean("isPublic"),
        isFriend = isFriend.toBoolean("isFriend"),
        isFamily = isFamily.toBoolean("isFamily"),
        owner = owner
    )

private fun PhotoDto.generatePhotoUrl(): String =
    "https://farm$farm.staticflickr.com/$server/${id}_$secret.jpg"

private fun Int.toBoolean(log: String): Boolean =
    when (this) {
        1 -> true
        0 -> false
        else -> throw IllegalArgumentException("$log value must be 0 or 1")
    }
