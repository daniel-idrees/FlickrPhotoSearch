package com.example.domain.usecase

import com.example.data.RepoPhotoSearchResult
import com.example.data.repository.PhotoRepository
import com.example.domain.PhotoSearchResult
import com.example.domain.model.toDomainModel
import javax.inject.Inject

internal class DefaultSearchPhotosUseCase @Inject constructor(
    private val photoRepository: PhotoRepository,
) : SearchPhotosUseCase {
    override suspend operator fun invoke(
        searchText: String,
    ): PhotoSearchResult {
        return when(val result = photoRepository.searchPhotos(searchText)) {
                 is RepoPhotoSearchResult.Success -> transformSuccessfulResult(result)
                 is RepoPhotoSearchResult.Error -> mapErrorResult(result)
             }
    }

    private fun transformSuccessfulResult(result: RepoPhotoSearchResult.Success): PhotoSearchResult {
        val photos = result.photos

        return if (photos.isNotEmpty()) {
            val searchedPhotos = photos.toDomainModel()
            PhotoSearchResult.Success(searchedPhotos)
        } else {
            PhotoSearchResult.Error.Empty
        }
    }

    private fun mapErrorResult(result: RepoPhotoSearchResult.Error): PhotoSearchResult {
        return when (result) {
            is RepoPhotoSearchResult.Error.RequestFailed -> PhotoSearchResult.Error.SearchFailed(result.errorMessage)
            RepoPhotoSearchResult.Error.ServerError -> PhotoSearchResult.Error.Generic
            RepoPhotoSearchResult.Error.NetworkUnavailable -> PhotoSearchResult.Error.NoInternetConnection
        }
    }
}
