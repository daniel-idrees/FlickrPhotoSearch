package com.example.ui.searchtabs

import app.cash.turbine.test
import com.example.domain.PhotoSearchResult
import com.example.domain.model.SearchedPhoto
import com.example.domain.usecase.SearchPhotosUseCase
import com.example.testfeature.rule.CoroutineTestRule
import com.example.ui.R
import com.example.ui.common.content.ContentErrorConfig
import com.example.ui.main.MainUiEffect
import com.example.ui.main.MainUiAction
import com.example.ui.main.MainViewModel
import com.example.ui.main.bottomtabs.screen.config.BottomBarScreen
import com.example.ui.util.anotherTestQuery
import com.example.ui.util.fakePhotoList
import com.example.ui.util.testQuery
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
internal class MainViewModelTest {
    @get:Rule
    val mainDispatcherRule = CoroutineTestRule()

    @Mock
    private lateinit var searchPhotosUseCase: SearchPhotosUseCase

    private val subject: MainViewModel by lazy {
        MainViewModel(searchPhotosUseCase)
    }

    @Test
    fun `Initial state should be set correctly`() = runTest {
        subject.viewState.value.isLoading shouldBe false
        subject.viewState.value.error shouldBe null
        subject.viewState.value.searchQuery shouldBe ""
        subject.viewState.value.lastSearch shouldBe ""
        subject.viewState.value.searchedPhotos shouldBe emptyList()
        subject.viewState.value.searchHistory shouldBe emptyList()
    }

    @Test
    fun `OnNavigateBackRequest event from Home should trigger navigation Pop effect`() = runTest {
        // when
        subject.setAction(MainUiAction.OnNavigateBackRequest(BottomBarScreen.Home))

        // then
        subject.effect.test {
            awaitItem() shouldBe MainUiEffect.Navigation.Pop(BottomBarScreen.Home)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `OnNavigateBackRequest event from Search should trigger navigation Pop effect`() = runTest {
        // when
        subject.setAction(MainUiAction.OnNavigateBackRequest(BottomBarScreen.Search))

        // then
        subject.effect.test {
            awaitItem() shouldBe MainUiEffect.Navigation.Pop(BottomBarScreen.Search)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `OnNavigateBackRequest event from History should trigger navigation Pop effect`() =
        runTest {
            // when
            subject.setAction(MainUiAction.OnNavigateBackRequest(BottomBarScreen.History))

            // then
            subject.effect.test {
                awaitItem() shouldBe MainUiEffect.Navigation.Pop(BottomBarScreen.History)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `ClearSearchHistory should trigger clear search history`() = runTest {

        whenever(searchPhotosUseCase(testQuery)).thenReturn(
            flowOf(
                PhotoSearchResult.Success(
                    fakePhotoList
                )
            )
        )
        // when
        subject.setAction(MainUiAction.RequestSearch(testQuery, BottomBarScreen.History))
        // then
        subject.viewState.value.searchHistory shouldBe listOf(testQuery)

        // when
        subject.setAction(MainUiAction.ClearSearchHistory)
        // then
        subject.viewState.value.searchHistory shouldBe emptyList()
    }

    @Test
    fun `RequestSearch should trigger search and switch to Search screen if fromScreen is Home`() =
        runTest {
            whenever(searchPhotosUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Success(
                        fakePhotoList
                    )
                )
            )
            // when
            subject.setAction(MainUiAction.RequestSearch(testQuery, BottomBarScreen.Home))

            // then
            verify(searchPhotosUseCase).invoke(testQuery)

            subject.effect.test {
                awaitItem() shouldBe MainUiEffect.Navigation.SwitchScreen(BottomBarScreen.Search)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `RequestSearch should trigger search and switch to Search screen if fromScreen is History`() =
        runTest {
            whenever(searchPhotosUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Success(
                        fakePhotoList
                    )
                )
            )
            // when
            subject.setAction(MainUiAction.RequestSearch(testQuery, BottomBarScreen.History))

            // then
            verify(searchPhotosUseCase).invoke(testQuery)

            subject.effect.test {
                awaitItem() shouldBe MainUiEffect.Navigation.SwitchScreen(BottomBarScreen.Search)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `RequestSearch from Search screen should trigger search but should not switch screen`() =
        runTest {
            whenever(searchPhotosUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Success(
                        fakePhotoList
                    )
                )
            )

            // when
            subject.setAction(MainUiAction.RequestSearch(testQuery, BottomBarScreen.Search))

            // then
            verify(searchPhotosUseCase).invoke(testQuery)

            subject.effect.test {
                expectNoEvents()
            }
        }

    @Test
    fun `RequestSearch should trigger ShowEmptyTextError is search query is empty`() = runTest {
        // when
        subject.setAction(MainUiAction.RequestSearch("", BottomBarScreen.Search))
        // then
        subject.effect.test {
            awaitItem() shouldBe MainUiEffect.ShowEmptyTextError(R.string.main_view_model_empty_text_error_toast_text)
            cancelAndIgnoreRemainingEvents()
        }

        // when white space characters
        subject.setAction(
            MainUiAction.RequestSearch(
                "   ",
                BottomBarScreen.Search
            )
        )

        // then
        subject.effect.test {
            awaitItem() shouldBe MainUiEffect.ShowEmptyTextError(R.string.main_view_model_empty_text_error_toast_text)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `RequestSearch should update lastSearch string in the viewState`() = runTest {
        whenever(searchPhotosUseCase(testQuery)).thenReturn(
            flowOf(
                PhotoSearchResult.Success(
                    fakePhotoList
                )
            )
        )

        // when
        subject.setAction(MainUiAction.RequestSearch(testQuery, BottomBarScreen.Search))

        // then
        subject.viewState.value.lastSearch shouldBe testQuery
    }

    @Test
    fun `OnSearchQueryChange should update the searchQuery string in the viewState`() = runTest {
        // when
        subject.setAction(MainUiAction.OnSearchQueryChange(testQuery))
        // then
        subject.viewState.value.searchQuery shouldBe testQuery

        // when
        subject.setAction(MainUiAction.OnSearchQueryChange(anotherTestQuery))
        // then
        subject.viewState.value.searchQuery shouldBe anotherTestQuery
    }

    @Test
    fun `RemoveSearchHistory should delete the saved query from search history`() = runTest {
        whenever(searchPhotosUseCase(testQuery)).thenReturn(
            flowOf(
                PhotoSearchResult.Success(
                    fakePhotoList
                )
            )
        )

        whenever(searchPhotosUseCase(anotherTestQuery)).thenReturn(
            flowOf(
                PhotoSearchResult.Success(
                    fakePhotoList
                )
            )
        )

        // when
        subject.setAction(MainUiAction.RequestSearch(testQuery, BottomBarScreen.History))
        subject.setAction(MainUiAction.RequestSearch(anotherTestQuery, BottomBarScreen.History))
        subject.viewState.value.searchHistory shouldBe listOf(
            anotherTestQuery,
            testQuery
        )

        subject.setAction(MainUiAction.RemoveSearchHistory(0))

        // then
        subject.viewState.value.searchHistory shouldBe listOf(testQuery)
    }

    @Test
    fun `OnSearchHistoryItemSelected should switch to Search screen if fromScreen is Home`() =
        runTest {
            whenever(searchPhotosUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Success(
                        fakePhotoList
                    )
                )
            )

            // when
            subject.setAction(
                MainUiAction.OnSearchHistoryItemSelected(
                    testQuery,
                    BottomBarScreen.Home
                )
            )

            // then
            verify(searchPhotosUseCase).invoke(testQuery)

            subject.effect.test {
                awaitItem() shouldBe MainUiEffect.Navigation.SwitchScreen(BottomBarScreen.Search)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `OnSearchHistoryItemSelected should switch to Search screen if fromScreen is History`() =
        runTest {
            whenever(searchPhotosUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Success(
                        fakePhotoList
                    )
                )
            )

            // when
            subject.setAction(
                MainUiAction.OnSearchHistoryItemSelected(
                    testQuery,
                    BottomBarScreen.History
                )
            )

            // then
            verify(searchPhotosUseCase).invoke(testQuery)

            subject.effect.test {
                awaitItem() shouldBe MainUiEffect.Navigation.SwitchScreen(BottomBarScreen.Search)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `OnSearchHistoryItemSelected from Search screen should not switch screen`() = runTest {
        whenever(searchPhotosUseCase(testQuery)).thenReturn(
            flowOf(
                PhotoSearchResult.Success(
                    fakePhotoList
                )
            )
        )

        // when
        subject.setAction(MainUiAction.OnSearchHistoryItemSelected(testQuery, BottomBarScreen.Search))

        // then
        verify(searchPhotosUseCase).invoke(testQuery)

        subject.effect.test {
            expectNoEvents()
        }
    }

    @Test
    fun `OnSearchHistoryItemSelected should trigger search if called triggered from Home`() =
        runTest {
            whenever(searchPhotosUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Success(
                        fakePhotoList
                    )
                )
            )

            // when
            subject.setAction(
                MainUiAction.OnSearchHistoryItemSelected(
                    testQuery,
                    BottomBarScreen.Home
                )
            )

            // then
            verify(searchPhotosUseCase).invoke(testQuery)
        }

    @Test
    fun `OnSearchHistoryItemSelected should trigger search if called triggered from Search`() =
        runTest {
            whenever(searchPhotosUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Success(
                        fakePhotoList
                    )
                )
            )

            // when
            subject.setAction(
                MainUiAction.OnSearchHistoryItemSelected(
                    testQuery,
                    BottomBarScreen.Search
                )
            )

            // then
            verify(searchPhotosUseCase).invoke(testQuery)
        }

    @Test
    fun `OnSearchHistoryItemSelected should trigger search if called triggered from History`() =
        runTest {
            whenever(searchPhotosUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Success(
                        fakePhotoList
                    )
                )
            )

            // when
            subject.setAction(
                MainUiAction.OnSearchHistoryItemSelected(
                    testQuery,
                    BottomBarScreen.History
                )
            )

            // then
            verify(searchPhotosUseCase).invoke(testQuery)
        }

    @Test
    fun `Error config in viewState should have relevant updates when photo list is empty in the result when OnSearchRequest triggers search`() =
        runTest {
            whenever(searchPhotosUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Success(
                        emptyList()
                    )
                )
            )

            // when
            subject.setAction(
                MainUiAction.RequestSearch(
                    searchQuery = testQuery,
                    fromScreen = BottomBarScreen.Search
                )
            )

            // then
            subject.viewState.value.lastSearch shouldBe testQuery
            subject.viewState.value.searchedPhotos shouldBe emptyList<SearchedPhoto>()
            subject.viewState.value.isLoading shouldBe false
            subject.viewState.value.searchHistory shouldBe listOf(testQuery)

            subject.viewState.value.error?.let { errorConfig ->
                errorConfig.errorTitleRes shouldBe ContentErrorConfig.ErrorMessage.Resource(R.string.main_view_model_empty_error_title)
                errorConfig.errorSubTitleRes shouldBe ContentErrorConfig.ErrorMessage.Resource(R.string.main_view_model_empty_error_sub_title)

                val capturedRetryFunction = errorConfig.onRetry

                capturedRetryFunction shouldNotBe null

                capturedRetryFunction?.invoke()

                subject.viewState.value.searchQuery shouldBe testQuery
            }
        }

    @Test
    fun `Error config in viewState should have photo list when use case returns success with populated photo list`() =
        runTest {
            whenever(searchPhotosUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Success(
                        fakePhotoList
                    )
                )
            )

            // when
            subject.setAction(
                MainUiAction.RequestSearch(
                    searchQuery = testQuery,
                    fromScreen = BottomBarScreen.Search
                )
            )

            // then
            subject.viewState.value.lastSearch shouldBe testQuery
            subject.viewState.value.searchedPhotos shouldBe fakePhotoList
            subject.viewState.value.error shouldBe null
            subject.viewState.value.isLoading shouldBe false
            subject.viewState.value.searchHistory shouldBe listOf(testQuery)
        }

    @Test
    fun `Error config in viewState should have relevant updates when use case returns generic error`() =
        runTest {

            whenever(searchPhotosUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Error.Generic
                )
            )

            // when
            subject.setAction(
                MainUiAction.RequestSearch(
                    searchQuery = testQuery,
                    fromScreen = BottomBarScreen.Search
                )
            )

            // then
            subject.viewState.value.lastSearch shouldBe testQuery
            subject.viewState.value.searchedPhotos shouldBe emptyList()
            subject.viewState.value.isLoading shouldBe false
            subject.viewState.value.searchHistory shouldBe listOf(testQuery)

            subject.viewState.value.error?.let { errorConfig ->
                errorConfig.errorTitleRes shouldBe ContentErrorConfig.ErrorMessage.Resource(R.string.main_view_model_generic_error_title)
                errorConfig.errorSubTitleRes shouldBe ContentErrorConfig.ErrorMessage.Resource(R.string.main_view_model_generic_error_sub_title)

                val capturedRetryFunction = errorConfig.onRetry

                capturedRetryFunction shouldNotBe null

                capturedRetryFunction?.invoke()

                subject.viewState.value.lastSearch shouldBe testQuery
                subject.viewState.value.searchHistory shouldBe listOf(
                    testQuery,
                    testQuery
                )

            }
        }

    @Test
    fun `Error config in viewState should have relevant updates when use case returns no result error`() =
        runTest {

            whenever(searchPhotosUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Error.SearchFailed("search failed")
                )
            )

            // when
            subject.setAction(
                MainUiAction.RequestSearch(
                    searchQuery = testQuery,
                    fromScreen = BottomBarScreen.Search
                )
            )

            // then
            subject.viewState.value.lastSearch shouldBe testQuery
            subject.viewState.value.searchedPhotos shouldBe emptyList()
            subject.viewState.value.isLoading shouldBe false
            subject.viewState.value.searchHistory shouldBe listOf(testQuery)

            subject.viewState.value.error?.let { errorConfig ->
                errorConfig.errorTitleRes shouldBe ContentErrorConfig.ErrorMessage.Resource(R.string.main_view_model_search_failed_error_title)
                errorConfig.errorSubTitleRes shouldBe ContentErrorConfig.ErrorMessage.Text("search failed")

                val capturedRetryFunction = errorConfig.onRetry

                capturedRetryFunction shouldNotBe null

                capturedRetryFunction?.invoke()

                subject.viewState.value.lastSearch shouldBe testQuery
                subject.viewState.value.searchHistory shouldBe listOf(
                    testQuery,
                    testQuery
                )
            }
        }


    @Test
    fun `Error config in viewState should have relevant updates when use case returns no internet connection error`() =
        runTest {

            whenever(searchPhotosUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Error.NoInternetConnection
                )
            )

            // when
            subject.setAction(
                MainUiAction.RequestSearch(
                    searchQuery = testQuery,
                    fromScreen = BottomBarScreen.Search
                )
            )

            // then
            subject.viewState.value.lastSearch shouldBe testQuery
            subject.viewState.value.searchedPhotos shouldBe emptyList()
            subject.viewState.value.isLoading shouldBe false
            subject.viewState.value.searchHistory shouldBe listOf(testQuery)

            subject.viewState.value.error?.let { errorConfig ->
                errorConfig.errorTitleRes shouldBe ContentErrorConfig.ErrorMessage.Resource(R.string.main_view_model_no_internet_connection_error_title)
                errorConfig.errorSubTitleRes shouldBe ContentErrorConfig.ErrorMessage.Resource(R.string.main_view_model_no_internet_connection_error_sub_title)

                val capturedRetryFunction = errorConfig.onRetry

                capturedRetryFunction shouldNotBe null

                capturedRetryFunction?.invoke()

                subject.viewState.value.lastSearch shouldBe testQuery
                subject.viewState.value.searchHistory shouldBe listOf(
                    testQuery,
                    testQuery
                )
            }
        }

    @Test
    fun `viewState should have empty photo list when use case returns generic error`() =
        runTest {
            whenever(searchPhotosUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Success(
                        fakePhotoList
                    )
                )
            )

            // when
            subject.setAction(
                MainUiAction.RequestSearch(
                    searchQuery = testQuery,
                    fromScreen = BottomBarScreen.Search
                )
            )

            // then

            subject.viewState.value.searchedPhotos shouldBe fakePhotoList

            whenever(searchPhotosUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Error.Generic
                )
            )

            // when
            subject.setAction(
                MainUiAction.RequestSearch(
                    searchQuery = testQuery,
                    fromScreen = BottomBarScreen.Search
                )
            )
            subject.viewState.value.searchedPhotos shouldBe emptyList()
        }

    @Test
    fun `viewState should have empty photo list when use case returns NoInternetConnection error`() =
        runTest {
            whenever(searchPhotosUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Success(
                        fakePhotoList
                    )
                )
            )

            // when
            subject.setAction(
                MainUiAction.RequestSearch(
                    searchQuery = testQuery,
                    fromScreen = BottomBarScreen.Search
                )
            )

            // then

            subject.viewState.value.searchedPhotos shouldBe fakePhotoList

            whenever(searchPhotosUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Error.NoInternetConnection
                )
            )

            // when
            subject.setAction(
                MainUiAction.RequestSearch(
                    searchQuery = testQuery,
                    fromScreen = BottomBarScreen.Search
                )
            )
            subject.viewState.value.searchedPhotos shouldBe emptyList()
        }

    @Test
    fun `viewState should have empty photo list when use case returns SearchFailed error`() =
        runTest {
            whenever(searchPhotosUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Success(
                        fakePhotoList
                    )
                )
            )

            // when
            subject.setAction(
                MainUiAction.RequestSearch(
                    searchQuery = testQuery,
                    fromScreen = BottomBarScreen.Search
                )
            )

            // then

            subject.viewState.value.searchedPhotos shouldBe fakePhotoList

            whenever(searchPhotosUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Error.SearchFailed("")
                )
            )

            // when
            subject.setAction(
                MainUiAction.RequestSearch(
                    searchQuery = testQuery,
                    fromScreen = BottomBarScreen.Search
                )
            )
            subject.viewState.value.searchedPhotos shouldBe emptyList()
        }

    @Test
    fun `viewState should have updated search history whenever OnSearchRequest triggers search`() =
        runTest {
            whenever(searchPhotosUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Success(
                        fakePhotoList
                    )
                )
            )

            whenever(searchPhotosUseCase(anotherTestQuery)).thenReturn(
                flowOf(PhotoSearchResult.Success(fakePhotoList))
            )

            // when
            subject.setAction(
                MainUiAction.RequestSearch(
                    searchQuery = testQuery,
                    fromScreen = BottomBarScreen.Search
                )
            )
            subject.setAction(MainUiAction.RequestSearch(anotherTestQuery, BottomBarScreen.Search))

            // then
            subject.viewState.value.searchHistory shouldBe listOf(
                anotherTestQuery,
                testQuery
            )
        }
}