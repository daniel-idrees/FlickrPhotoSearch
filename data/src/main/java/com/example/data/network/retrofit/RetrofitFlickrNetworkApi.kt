package com.example.data.network.retrofit

import com.example.data.network.FlickrNetworkDataSource
import com.example.data.network.dto.PhotoSearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton
import com.example.data.BuildConfig
import retrofit2.Retrofit

private const val API_KEY = BuildConfig.API_KEY

/**
 * Retrofit API declaration for Retrofit Network API
 */
internal interface RetrofitFlickrNetworkApi {
    @GET("services/rest/")
    suspend fun getPhotos(
        @Query("text") searchText: String,
        @Query("method") method: String,
        @Query("api_key") apiKey: String,
        @Query("format") format: String,
        @Query("nojsoncallback") noJsonCallback: Int,
        @Query("safe_search") safeSearch: Int
    ): PhotoSearchResponseDto
}


/**
 * [Retrofit] backed [FlickrNetworkDataSource]
 */
@Singleton
internal class RetrofitFlickrApiClient @Inject constructor(
    private val networkApi: RetrofitFlickrNetworkApi
) : FlickrNetworkDataSource {
    override suspend fun searchPhotos(searchText: String): PhotoSearchResponseDto =
        networkApi.getPhotos(
            searchText = searchText,
            method = "flickr.photos.search",
            apiKey = API_KEY,
            format = "json",
            noJsonCallback = 1,
            safeSearch = 1,
        )
}