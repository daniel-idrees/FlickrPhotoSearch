package com.example.domain.model

import com.example.data.repository.model.Photo
import com.example.data.repository.model.PhotoVisibility

internal fun List<Photo>.toDomainModel(): List<SearchedPhoto> =
    this.map { photo ->
        with(photo) {
            SearchedPhoto(
                title = title,
                url = url,
                visibility = getVisibility(photoVisibility),
            )
        }
    }


private fun getVisibility(visibility: PhotoVisibility): Visibility {
    return when (visibility) {
        PhotoVisibility.PUBLIC -> Visibility.PUBLIC
        PhotoVisibility.FRIEND -> Visibility.FRIEND
        PhotoVisibility.FAMILY -> Visibility.FAMILY
        PhotoVisibility.PRIVATE -> Visibility.PRIVATE
    }
}


