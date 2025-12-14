package com.example.domain.usecase

import com.example.data.RepoPhotoSearchResult
import com.example.data.repository.PhotoRepository
import com.example.data.repository.model.Photo
import com.example.domain.PhotoSearchResult
import com.example.domain.util.fakePhotoDtoList
import com.example.domain.util.fakeSearchedPhotos
import com.example.testfeature.rule.CoroutineTestRule
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class SearchPhotosUseCaseTest {
    @Mock
    private lateinit var repository: PhotoRepository

    @get:Rule
    val mainDispatcherRule = CoroutineTestRule()

    private val subject: SearchPhotosUseCase by lazy {
        DefaultSearchPhotosUseCase(repository)
    }

    @Test
    fun `Invocation should return Success if the repository returns the populated photo list`() =
        runTest {
            // when
            whenever(repository.searchPhotos("text")) doReturn
                    RepoPhotoSearchResult.Success(fakePhotoDtoList)

            val expected = PhotoSearchResult.Success(fakeSearchedPhotos)

            // then
            val result = subject("text")

            // then
            result shouldBe expected

            verify(repository).searchPhotos("text")
            verifyNoMoreInteractions(repository)

            fakeSearchedPhotos.shouldNotBeEmpty()
        }


    @Test
    fun `Invocation should return Empty if the repository returns the empty photo list`() =
        runTest {
            // when
            val photoDtoList: List<Photo> = emptyList()

            whenever(repository.searchPhotos("text")) doReturn
                    RepoPhotoSearchResult.Success(photoDtoList)
            val expected = PhotoSearchResult.Error.Empty
            val result = subject("text")

            // then
            result shouldBe expected

            verify(repository).searchPhotos("text")
            verifyNoMoreInteractions(repository)
        }

    @Test
    fun `Invocation should return Generic error if the repository returns ServerError`() = runTest {
        // when
        whenever(repository.searchPhotos("text")) doReturn RepoPhotoSearchResult.Error.ServerError

        val expected = PhotoSearchResult.Error.Generic
        val result = subject("text")

        // then
        result shouldBe expected

        verify(repository).searchPhotos("text")
        verifyNoMoreInteractions(repository)
    }

    @Test
    fun `Invocation should return SearchFailed Error if the repository returns invalid status`() =
        runTest {
            // when
            whenever(repository.searchPhotos("text")) doReturn
                    RepoPhotoSearchResult.Error.RequestFailed(
                        "request failed"
                    )

            val expected = PhotoSearchResult.Error.SearchFailed("request failed")
            val result = subject("text")

            // then
            result shouldBe expected

            verify(repository).searchPhotos("text")
            verifyNoMoreInteractions(repository)
        }

    @Test
    fun `Invocation should return Error if the repository returns network unavailable error`() =
        runTest {
            // when
            whenever(repository.searchPhotos("text")) doReturn RepoPhotoSearchResult.Error.NetworkUnavailable

            val expected = PhotoSearchResult.Error.NoInternetConnection
            val result = subject("text")

            // then
            result shouldBe expected

            verify(repository).searchPhotos("text")
            verifyNoMoreInteractions(repository)
        }
}