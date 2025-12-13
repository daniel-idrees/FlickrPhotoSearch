package com.example.data.di

import com.example.data.BuildConfig
import com.example.data.network.PhotoDataSource
import com.example.data.network.retrofit.RetrofitFlickrApiClient
import com.example.data.network.retrofit.RetrofitFlickrNetworkApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Call
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
    fun provideConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

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
        converterFactory: GsonConverterFactory
    ): RetrofitFlickrNetworkApi =
        retrofitBuilder
            .baseUrl("https://api.flickr.com/")
            .callFactory { okhttpCallFactory.get().newCall(it) }
            .addConverterFactory(converterFactory)
            .build()
            .create(RetrofitFlickrNetworkApi::class.java)

    @Provides
    @Singleton
    fun providePhotoDataSource(
        network: RetrofitFlickrApiClient
    ): PhotoDataSource = network
}
