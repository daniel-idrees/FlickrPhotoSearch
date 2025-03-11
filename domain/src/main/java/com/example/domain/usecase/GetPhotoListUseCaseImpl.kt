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
                    is RepoPhotoSearchResult.Error -> handleErrorResult(result)
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

    private fun handleErrorResult(result: RepoPhotoSearchResult.Error): PhotoSearchResult {
        return when (result) {
            is RepoPhotoSearchResult.Error.RequestFailed -> PhotoSearchResult.Error.SearchFailed(result.errorMessage)
            RepoPhotoSearchResult.Error.ServerError -> PhotoSearchResult.Error.Generic
            RepoPhotoSearchResult.Error.NetworkUnavailable -> PhotoSearchResult.Error.NoInternetConnection
        }
    }
}
