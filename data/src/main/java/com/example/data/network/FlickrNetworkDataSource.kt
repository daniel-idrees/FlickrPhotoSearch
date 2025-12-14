package com.example.data.network

import com.example.data.network.dto.PhotoSearchResponseDto

/**
 * Interface representing network calls to the Flickr backend
 */
internal interface FlickrNetworkDataSource {
    suspend fun searchPhotos(searchText: String): PhotoSearchResponseDto
}