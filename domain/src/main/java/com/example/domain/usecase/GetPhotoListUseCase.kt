package com.example.domain.usecase

import com.example.domain.PhotoSearchResult
import kotlinx.coroutines.flow.Flow

interface GetPhotoListUseCase {
    suspend operator fun invoke(
        searchText: String,
    ): Flow<PhotoSearchResult>
}