package com.example.data.repository

import com.example.data.RepoPhotoSearchResult
import com.example.data.dto.mapper.toPhotoList
import com.example.data.dto.mapper.translateErrorCodeMessage
import com.example.data.network.FlickrNetworkDataSource
import com.example.data.runSuspendCatching
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

/**
 * Repository that fetches photos from network
 */
internal class PhotoNetworkRepository @Inject constructor(
    private val flickrNetworkDataSource: FlickrNetworkDataSource
) : PhotoRepository {
    override suspend fun searchPhotos(searchText: String): Flow<RepoPhotoSearchResult> =
        flow {
            runSuspendCatching {
                val response = flickrNetworkDataSource.searchPhotos(searchText)

                if (response.status != "ok") {
                    //log the response and code
                    emit(
                        RepoPhotoSearchResult.Error.RequestFailed(
                            errorMessage = translateErrorCodeMessage(response.errorCode)
                        )
                    )
                    return@flow
                }

                val photos = response.toPhotoList()
                photos
            }.onSuccess { photos ->
                emit(RepoPhotoSearchResult.Success(photos))
            }.onFailure {
                //log failure
                emit(getErrorResult(it))
            }
        }

    private fun getErrorResult(throwable: Throwable): RepoPhotoSearchResult {
        return when (throwable) {
            is IOException -> RepoPhotoSearchResult.Error.NetworkUnavailable
            else -> RepoPhotoSearchResult.Error.ServerError
        }
    }
}