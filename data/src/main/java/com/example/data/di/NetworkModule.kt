package com.example.data.di

import com.example.data.BuildConfig
import com.example.data.network.PhotoDataSource
import com.example.data.network.retrofit.RetrofitFlickApiClient
import com.example.data.network.retrofit.RetrofitFlickrApi
import com.example.data.network.retrofit.RetrofitFlickrApi.Companion.FLICKR_API_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class NetworkModule {

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
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient()
            .newBuilder()
            .addInterceptor(httpLoggingInterceptor).build()

    @Provides
    fun provideConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun provideRetrofitBuilder(
        client: OkHttpClient,
        converterFactory: GsonConverterFactory
    ): Retrofit.Builder =
        Retrofit.Builder()
            .addConverterFactory(converterFactory)
            .client(client)

    @Provides
    @Singleton
    fun provideRetrofitFlickrApi(
        retrofitBuilder: Retrofit.Builder,
    ): RetrofitFlickrApi =
        retrofitBuilder
            .baseUrl(FLICKR_API_BASE_URL)
            .build()
            .create(RetrofitFlickrApi::class.java)

    @Provides
    @Singleton
    fun providePhotoDataSource(
        network: RetrofitFlickApiClient
    ): PhotoDataSource = network
}
