package com.example.data.network.retrofit

import com.example.data.network.PhotoDataSource
import com.example.data.network.model.PhotoSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton
import com.example.data.BuildConfig

private const val API_KEY = BuildConfig.API_KEY

internal interface RetrofitFlickrApi {
    companion object {
        const val FLICKR_API_BASE_URL = "https://api.flickr.com/"
    }

    @GET("services/rest/")
    suspend fun callFlickr(
        @Query("text") searchText: String,
        @Query("method") method: String,
        @Query("api_key") apiKey: String,
        @Query("format") format: String,
        @Query("nojsoncallback") noJsonCallback: Int,
        @Query("safe_search") safeSearch: Int
    ): PhotoSearchResponse
}

@Singleton
internal class RetrofitFlickApiClient @Inject constructor(
    private val networkApi: RetrofitFlickrApi
) : PhotoDataSource {
    override suspend fun searchPhotos(searchText: String): PhotoSearchResponse =
        networkApi.callFlickr(
            searchText = searchText,
            method = "flickr.photos.search",
            apiKey = API_KEY,
            format = "json",
            noJsonCallback = 1,
            safeSearch = 1,
        )
}