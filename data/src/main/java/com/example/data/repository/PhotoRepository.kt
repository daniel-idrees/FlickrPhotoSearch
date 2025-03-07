package com.example.data.repository

import com.example.data.RepoPhotoSearchResult
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {
    suspend fun searchPhotos(searchText: String): Flow<RepoPhotoSearchResult>
}