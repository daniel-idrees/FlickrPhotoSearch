package com.example.domain.usecase

import app.cash.turbine.test
import com.example.data.RepoPhotoSearchResult
import com.example.data.dto.PhotoItemDto
import com.example.data.repository.PhotoRepository
import com.example.domain.PhotoSearchResult
import com.example.domain.util.fakePhotoDtoList
import com.example.domain.util.fakePhotoList
import com.example.testfeature.rule.CoroutineTestRule
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.flowOf
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
class GetPhotoListUseCaseTest {
    @Mock
    private lateinit var repository: PhotoRepository

    @get:Rule
    val mainDispatcherRule = CoroutineTestRule()

    private val subject: GetPhotoListUseCase by lazy {
        GetPhotoListUseCaseImpl(repository)
    }

    @Test
    fun `Invocation should return Success if the repository returns the populated photo list`() =
        runTest {
            // when
            whenever(repository.searchPhotos("text")) doReturn flowOf(
                RepoPhotoSearchResult.Success(fakePhotoDtoList)
            )

            // then
            subject("text").test {
                verify(repository).searchPhotos("text")
                verifyNoMoreInteractions(repository)
                val result = awaitItem()
                result shouldBe PhotoSearchResult.Success(fakePhotoList)
                fakePhotoList.shouldNotBeEmpty()

                cancelAndIgnoreRemainingEvents()
            }
        }


    @Test
    fun `Invocation should return Empty if the repository returns the empty photo list`() =
        runTest {
            // when
            val photoDtoList: List<PhotoItemDto> = emptyList()

            whenever(repository.searchPhotos("text")) doReturn flowOf(
                RepoPhotoSearchResult.Success(photoDtoList)
            )

            // then
            subject("text").test {
                verify(repository).searchPhotos("text")
                verifyNoMoreInteractions(repository)
                val result = awaitItem()
                result shouldBe PhotoSearchResult.Empty

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `Invocation should return Error if the repository returns error`() = runTest {
        // when
        whenever(repository.searchPhotos("text")) doReturn flowOf(RepoPhotoSearchResult.Error)

        // then
        subject("text").test {
            verify(repository).searchPhotos("text")
            verifyNoMoreInteractions(repository)
            val result = awaitItem()
            result shouldBe PhotoSearchResult.Error.Generic
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Invocation should return Error if the repository returns invalid status`() = runTest {
        // when
        whenever(repository.searchPhotos("text")) doReturn flowOf(RepoPhotoSearchResult.RequestFailed)

        // then
        subject("text").test {
            verify(repository).searchPhotos("text")
            verifyNoMoreInteractions(repository)
            val result = awaitItem()
            result shouldBe PhotoSearchResult.Error.SearchFailed
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Invocation should return Error if the repository returns network unavailable error`() =
        runTest {
            // when
            whenever(repository.searchPhotos("text")) doReturn flowOf(RepoPhotoSearchResult.NetworkUnavailable)

            // then
            subject("text").test {
                verify(repository).searchPhotos("text")
                verifyNoMoreInteractions(repository)
                val result = awaitItem()
                result shouldBe PhotoSearchResult.Error.NoInternetConnection
                cancelAndIgnoreRemainingEvents()
            }
        }
}