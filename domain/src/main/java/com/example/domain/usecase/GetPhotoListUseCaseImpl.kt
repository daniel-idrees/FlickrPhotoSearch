package com.example.domain.usecase

import com.example.data.RepoPhotoSearchResult
import com.example.data.repository.PhotoRepository
import com.example.data.runSuspendCatching
import com.example.domain.PhotoSearchResult
import com.example.domain.model.toDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GetPhotoListUseCaseImpl @Inject constructor(
    private val photoRepository: PhotoRepository,
) : GetPhotoListUseCase {
    override suspend operator fun invoke(
        searchText: String,
    ): Flow<PhotoSearchResult> = flow {
        runSuspendCatching {
            photoRepository.searchPhotos(searchText).map { result ->
                when (result) {
                    is RepoPhotoSearchResult.Success -> handleSuccessResult(result)
                    RepoPhotoSearchResult.Error -> PhotoSearchResult.Error.Generic
                    RepoPhotoSearchResult.InvalidStatus -> PhotoSearchResult.Error.NoResult
                    RepoPhotoSearchResult.NetworkUnavailable -> PhotoSearchResult.Error.NoInternetConnection
                }
            }
        }.onSuccess { result ->
            result.collect(::emit)
        }.onFailure {
            emit(PhotoSearchResult.Error.Generic)
        }
    }

    private fun handleSuccessResult(result: RepoPhotoSearchResult.Success): PhotoSearchResult {
        val photos = result.photos.toDomainModel()

        return if (photos.isNotEmpty()) {
            PhotoSearchResult.Success(photos)
        } else {
            PhotoSearchResult.Empty
        }
    }
}
