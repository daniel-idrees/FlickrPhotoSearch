package com.example.data

import com.example.data.dto.PhotoItemDto


sealed interface RepoPhotoSearchResult {
    data class Success(val photos: List<PhotoItemDto>) : RepoPhotoSearchResult
    sealed interface Error : RepoPhotoSearchResult {
        data object NetworkUnavailable : Error
        data class RequestFailed(val errorMessage: String) : Error
        data object ServerError : Error
    }
}