package com.example.domain

import com.example.domain.model.SearchedPhoto

sealed interface PhotoSearchResult {
    data class Success(val searchedPhotos: List<SearchedPhoto>) : PhotoSearchResult
    sealed interface Error : PhotoSearchResult {
        data object Generic : Error
        data class SearchFailed(val errorMessage: String) : Error
        data object NoInternetConnection : Error
    }

    data object Empty : PhotoSearchResult
}