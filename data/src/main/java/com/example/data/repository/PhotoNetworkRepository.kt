package com.example.data.repository

import com.example.data.RepoPhotoSearchResult
import com.example.data.network.FlickrNetworkDataSource
import com.example.data.network.dto.mapper.toPhotoList
import com.example.data.network.dto.mapper.translateErrorCodeMessage
import com.example.data.network.exception.InvalidStatusException
import com.example.data.runSuspendCatching
import java.io.IOException
import javax.inject.Inject

/**
 * Repository that fetches photos from network
 */
internal class PhotoNetworkRepository @Inject constructor(
    private val flickrNetworkDataSource: FlickrNetworkDataSource,
) : PhotoRepository {
    override suspend fun searchPhotos(searchText: String): RepoPhotoSearchResult {
        return runSuspendCatching {
            val response = flickrNetworkDataSource.searchPhotos(searchText)

            if (response.status != "ok") {
                throw InvalidStatusException(response.errorCode)
            }

            response
        }.fold(
            onSuccess = { response ->
                val photos = response.toPhotoList()
                RepoPhotoSearchResult.Success(photos)
            },
            onFailure = { throwable ->
                getErrorResult(throwable)
            }
        )
    }

    private fun getErrorResult(throwable: Throwable): RepoPhotoSearchResult {
        return when (throwable) {
            is InvalidStatusException -> RepoPhotoSearchResult.Error.RequestFailed(
                errorMessage = translateErrorCodeMessage(
                    throwable.errorCode
                )
            )

            is IOException -> RepoPhotoSearchResult.Error.NetworkUnavailable
            else -> RepoPhotoSearchResult.Error.ServerError
        }
    }
}