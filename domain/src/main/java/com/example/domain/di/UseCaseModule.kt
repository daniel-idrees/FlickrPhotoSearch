package com.example.domain.di

import com.example.data.repository.PhotoRepository
import com.example.domain.usecase.GetPhotoListUseCase
import com.example.domain.usecase.GetPhotoListUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
internal class UseCaseModule {
    @Provides
    @ViewModelScoped
    fun providesGetPhotoListUseCase(
        photoRepository: PhotoRepository
    ): GetPhotoListUseCase = GetPhotoListUseCaseImpl(photoRepository)
}
