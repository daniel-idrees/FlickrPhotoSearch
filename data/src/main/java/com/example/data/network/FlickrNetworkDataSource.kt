package com.example.data.network

import com.example.data.network.model.PhotoSearchResponse

/**
 * Interface representing network calls to the Flickr backend
 */
internal interface FlickrNetworkDataSource {
    suspend fun searchPhotos(searchText: String): PhotoSearchResponse
}