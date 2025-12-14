package com.example.domain.usecase

import com.example.domain.PhotoSearchResult
import kotlinx.coroutines.flow.Flow

/**
 * A use case which obtains a list of photos for a specific search text
 */
interface GetPhotosUseCase {
    /**
     * Returns a list of photos for a specific search text
     */
    suspend operator fun invoke(
        searchText: String,
    ): Flow<PhotoSearchResult>
}