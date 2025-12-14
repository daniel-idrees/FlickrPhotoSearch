package com.example.domain.model

import com.example.data.repository.model.Photo

internal fun List<Photo>.toDomainModel(): List<SearchedPhoto> =
    this.map { photo ->
        with(photo) {
            SearchedPhoto(
                title = title,
                url = url,
                isPublic = isPublic,
                isFriend = isFriend,
                isFamily = isFamily
            )
        }
    }


