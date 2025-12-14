package com.example.data.network.dto.mapper

import com.example.data.repository.model.Photo
import com.example.data.network.dto.PhotoDto
import com.example.data.network.dto.PhotoSearchResponseDto
import com.example.data.repository.model.PhotoVisibility

internal fun PhotoSearchResponseDto.toPhotoList(): List<Photo> =
    this.photoSearchDetailDto.photosDto.map { photo ->
        photo.toExternalModel()
    }


private fun PhotoDto.toExternalModel(): Photo =
    Photo(
        title = title,
        url = generatePhotoUrl(),
        photoVisibility = getVisibility(),
        owner = owner
    )


private fun PhotoDto.getVisibility(): PhotoVisibility {
    return when {
        this.isFamily.toBoolean("isFamily") -> PhotoVisibility.FAMILY
        this.isFriend.toBoolean("isFriend") -> PhotoVisibility.FRIEND
        this.isPublic.toBoolean("isPublic") -> PhotoVisibility.PUBLIC
        else -> PhotoVisibility.PRIVATE
    }
}

private fun PhotoDto.generatePhotoUrl(): String =
    "https://farm$farm.staticflickr.com/$server/${id}_$secret.jpg"

private fun Int.toBoolean(log: String): Boolean =
    when (this) {
        1 -> true
        0 -> false
        else -> throw IllegalArgumentException("$log value must be 0 or 1")
    }
