package com.example.domain.di

import com.example.domain.usecase.GetPhotoListUseCase
import com.example.domain.usecase.GetPhotoListUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Singleton


@Module
@InstallIn(ViewModelComponent::class)
internal interface UseCaseModule {
    @Binds
    @Singleton
    fun providesUseCase(
        impl: GetPhotoListUseCaseImpl,
    ): GetPhotoListUseCase
}