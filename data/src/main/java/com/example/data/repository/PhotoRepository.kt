package com.example.data.repository

import com.example.data.RepoPhotoSearchResult
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {
    /**
     * Gets photos for a specific search text
     */
    suspend fun searchPhotos(searchText: String): Flow<RepoPhotoSearchResult>
}