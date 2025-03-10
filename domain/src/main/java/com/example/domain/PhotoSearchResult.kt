package com.example.domain

import com.example.domain.model.PhotoItem

sealed interface PhotoSearchResult {
    data class Success(val photos: List<PhotoItem>) : PhotoSearchResult
    sealed interface Error : PhotoSearchResult {
        data object Generic : Error
        data object NoResult : Error
        data object NoInternetConnection : Error
    }

    data object Empty : PhotoSearchResult
}