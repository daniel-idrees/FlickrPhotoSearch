package com.example.data

import com.example.data.dto.PhotoItemDto


sealed interface RepoPhotoSearchResult {
    data class Success(val photos: List<PhotoItemDto>) : RepoPhotoSearchResult
    data object Error : RepoPhotoSearchResult
    data object NetworkUnavailable : RepoPhotoSearchResult
    data object InvalidStatus : RepoPhotoSearchResult
}