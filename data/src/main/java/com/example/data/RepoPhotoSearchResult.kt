package com.example.data

import com.example.data.repository.model.Photo


sealed interface RepoPhotoSearchResult {
    data class Success(val photos: List<Photo>) : RepoPhotoSearchResult
    sealed interface Error : RepoPhotoSearchResult {
        data object NetworkUnavailable : Error
        data class RequestFailed(val errorMessage: String) : Error
        data object ServerError : Error
    }
}