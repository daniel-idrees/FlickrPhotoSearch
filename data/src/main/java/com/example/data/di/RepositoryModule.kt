package com.example.data.di

import com.example.data.repository.PhotoNetworkRepository
import com.example.data.repository.PhotoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface RepositoryModule {
    @Binds
    @Singleton
    fun providesPhotoRepository(
        photoNetworkRepository: PhotoNetworkRepository,
    ): PhotoRepository
}