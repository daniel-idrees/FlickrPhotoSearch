package com.example.domain

import com.example.domain.model.PhotoItem

sealed interface PhotoSearchResult {
    data class Success(val photos: List<PhotoItem>) : PhotoSearchResult
    data class Error(var errorMessage: String) : PhotoSearchResult
}