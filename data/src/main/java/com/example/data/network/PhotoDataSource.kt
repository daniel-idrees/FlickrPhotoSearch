package com.example.data.network

import com.example.data.network.model.PhotoSearchResponse

internal interface PhotoDataSource {
    suspend fun searchPhotos(searchText: String): PhotoSearchResponse
}