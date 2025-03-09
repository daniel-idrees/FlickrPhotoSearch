package com.example.data.repository

import com.example.data.RepoPhotoSearchResult
import com.example.data.dto.mapper.toPhotoList
import com.example.data.network.PhotoDataSource
import com.example.data.runSuspendCatching
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

internal class PhotoNetworkRepository @Inject constructor(
    private val photoDataSource: PhotoDataSource
) : PhotoRepository {
    override suspend fun searchPhotos(searchText: String): Flow<RepoPhotoSearchResult> =
        flow {
            runSuspendCatching {
                val response = photoDataSource.searchPhotos(searchText)

                if (response.status != "ok") {
                    emit(RepoPhotoSearchResult.InvalidStatus)
                }

                val photos = response.toPhotoList()
                photos
            }.onSuccess { photos ->
                emit(RepoPhotoSearchResult.Success(photos))
            }.onFailure {
                emit(getErrorResult(it))
            }
        }

    private fun getErrorResult(throwable: Throwable): RepoPhotoSearchResult {
        return when (throwable) {
            is IOException -> RepoPhotoSearchResult.NetworkUnavailable
            else -> RepoPhotoSearchResult.Error
        }
    }
}