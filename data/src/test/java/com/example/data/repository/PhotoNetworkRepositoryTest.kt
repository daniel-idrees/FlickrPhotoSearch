package com.example.data.repository

import app.cash.turbine.test
import com.example.data.RepoPhotoSearchResult
import com.example.data.network.PhotoDataSource
import com.example.data.util.fakePhotoItemDtoList
import com.example.data.util.fakePhotoSearchResponse
import com.example.data.util.fakePhotoSearchResponseWithInvalidStatus
import com.example.testfeature.rule.CoroutineTestRule
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class PhotoNetworkRepositoryTest {
    @get:Rule
    val mainDispatcherRule = CoroutineTestRule()

    @Mock
    private lateinit var network: PhotoDataSource

    private val subject by lazy { PhotoNetworkRepository(network) }

    @Test
    fun `searchPhotos should return success when api response is successful`() =
        runTest {
            whenever(network.searchPhotos("test")) doReturn fakePhotoSearchResponse

            val result = subject.searchPhotos("test")

            // when
            val expected = RepoPhotoSearchResult.Success(fakePhotoItemDtoList)

            // then
            result.test {
                awaitItem() shouldBe expected
                expected.photos.size shouldBe fakePhotoSearchResponse.photoSearchDetail.photos.size
                verify(network).searchPhotos("test")
                verifyNoMoreInteractions(network)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `searchPhotos should return success when api response is successful but status is invalid`() =
        runTest {
            whenever(network.searchPhotos("test")) doReturn fakePhotoSearchResponseWithInvalidStatus

            // when
            val result = subject.searchPhotos("test")

            // then
            result.test {
                awaitItem() shouldBe RepoPhotoSearchResult.Error.RequestFailed("Operation failed. Please contact support.")
                verify(network).searchPhotos("test")
                verifyNoMoreInteractions(network)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `searchPhotos should return ServerError when api response throws general exception`() =
        runTest {
            whenever(network.searchPhotos("test")) doThrow IllegalArgumentException()

            // when
            val result = subject.searchPhotos("test")

            // then
            result.test {
                awaitItem() shouldBe RepoPhotoSearchResult.Error.ServerError
                verify(network).searchPhotos("test")
                verifyNoMoreInteractions(network)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `searchPhotos should return NetworkUnavailable error when api response throws IOException`() =
        runTest {
            whenever(network.searchPhotos("test")) doAnswer { throw IOException() }

            // when
            val result = subject.searchPhotos("test")

            // then
            result.test {
                awaitItem() shouldBe RepoPhotoSearchResult.Error.NetworkUnavailable
                verify(network).searchPhotos("test")
                verifyNoMoreInteractions(network)
                cancelAndIgnoreRemainingEvents()
            }
        }
}