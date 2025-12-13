package com.example.data.di

import com.example.data.BuildConfig
import com.example.data.network.FlickrNetworkDataSource
import com.example.data.network.retrofit.RetrofitFlickrApiClient
import com.example.data.network.retrofit.RetrofitFlickrNetworkApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class NetworkModule {

    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    @Provides
    @Singleton
    fun provideRetrofitBuilder(): Retrofit.Builder = Retrofit.Builder()

    @Provides
    @Singleton
    fun okHttpCallFactory(httpLoggingInterceptor: HttpLoggingInterceptor): Call.Factory = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideRetrofitFlickrNetworkApi(
        retrofitBuilder: Retrofit.Builder,
        okhttpCallFactory: dagger.Lazy<Call.Factory>,
        networkJson: Json
    ): RetrofitFlickrNetworkApi =
        retrofitBuilder
            .baseUrl("https://api.flickr.com/")
            .callFactory { okhttpCallFactory.get().newCall(it) }
            .addConverterFactory(
                networkJson.asConverterFactory("application/json".toMediaType()),
            )
            .build()
            .create(RetrofitFlickrNetworkApi::class.java)

    @Provides
    @Singleton
    fun providePhotoDataSource(
        network: RetrofitFlickrApiClient
    ): FlickrNetworkDataSource = network
}
