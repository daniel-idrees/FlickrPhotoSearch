package com.example.ui.searchtabs

import app.cash.turbine.test
import com.example.domain.PhotoSearchResult
import com.example.domain.model.Photo
import com.example.domain.usecase.GetPhotoListUseCase
import com.example.testfeature.rule.CoroutineTestRule
import com.example.ui.R
import com.example.ui.common.content.ContentErrorConfig
import com.example.ui.main.MainUiEffect
import com.example.ui.main.MainUiEvent
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
    private lateinit var getPhotoListUseCase: GetPhotoListUseCase

    private val subject: MainViewModel by lazy {
        MainViewModel(getPhotoListUseCase)
    }

    @Test
    fun `Initial state should be set correctly`() = runTest {
        subject.viewState.value.isLoading shouldBe false
        subject.viewState.value.error shouldBe null
        subject.viewState.value.searchQuery shouldBe ""
        subject.viewState.value.lastSearch shouldBe ""
        subject.viewState.value.photoList shouldBe emptyList()
        subject.viewState.value.searchResultTitleRes shouldBe 0
        subject.viewState.value.searchHistory shouldBe ArrayDeque()
    }

    @Test
    fun `OnNavigateBackRequest event from Home should trigger navigation Pop effect`() = runTest {
        // when
        subject.setEvent(MainUiEvent.OnNavigateBackRequest(BottomBarScreen.Home))

        // then
        subject.effect.test {
            awaitItem() shouldBe MainUiEffect.Navigation.Pop(BottomBarScreen.Home)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `OnNavigateBackRequest event from Search should trigger navigation Pop effect`() = runTest {
        // when
        subject.setEvent(MainUiEvent.OnNavigateBackRequest(BottomBarScreen.Search))

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
            subject.setEvent(MainUiEvent.OnNavigateBackRequest(BottomBarScreen.History))

            // then
            subject.effect.test {
                awaitItem() shouldBe MainUiEffect.Navigation.Pop(BottomBarScreen.History)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `ClearSearchHistory should trigger clear search history`() = runTest {

        whenever(getPhotoListUseCase(testQuery)).thenReturn(
            flowOf(
                PhotoSearchResult.Success(
                    fakePhotoList
                )
            )
        )
        // when
        subject.setEvent(MainUiEvent.RequestSearch(testQuery, BottomBarScreen.History))
        // then
        subject.viewState.value.searchHistory shouldBe ArrayDeque(listOf(testQuery))

        // when
        subject.setEvent(MainUiEvent.ClearSearchHistory)
        // then
        subject.viewState.value.searchHistory shouldBe ArrayDeque()
    }

    @Test
    fun `RequestSearch should trigger search and switch to Search screen if fromScreen is Home`() =
        runTest {
            whenever(getPhotoListUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Success(
                        fakePhotoList
                    )
                )
            )
            // when
            subject.setEvent(MainUiEvent.RequestSearch(testQuery, BottomBarScreen.Home))

            // then
            verify(getPhotoListUseCase).invoke(testQuery)

            subject.effect.test {
                awaitItem() shouldBe MainUiEffect.Navigation.SwitchScreen(BottomBarScreen.Search)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `RequestSearch should trigger search and switch to Search screen if fromScreen is History`() =
        runTest {
            whenever(getPhotoListUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Success(
                        fakePhotoList
                    )
                )
            )
            // when
            subject.setEvent(MainUiEvent.RequestSearch(testQuery, BottomBarScreen.History))

            // then
            verify(getPhotoListUseCase).invoke(testQuery)

            subject.effect.test {
                awaitItem() shouldBe MainUiEffect.Navigation.SwitchScreen(BottomBarScreen.Search)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `RequestSearch from Search screen should trigger search but should not switch screen`() =
        runTest {
            whenever(getPhotoListUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Success(
                        fakePhotoList
                    )
                )
            )

            // when
            subject.setEvent(MainUiEvent.RequestSearch(testQuery, BottomBarScreen.Search))

            // then
            verify(getPhotoListUseCase).invoke(testQuery)

            subject.effect.test {
                expectNoEvents()
            }
        }

    @Test
    fun `RequestSearch should trigger ShowEmptyTextError is search query is empty`() = runTest {
        // when
        subject.setEvent(MainUiEvent.RequestSearch("", BottomBarScreen.Search))
        // then
        subject.effect.test {
            awaitItem() shouldBe MainUiEffect.ShowEmptyTextError(R.string.main_view_model_empty_text_error_toast_text)
            cancelAndIgnoreRemainingEvents()
        }

        // when white space characters
        subject.setEvent(
            MainUiEvent.RequestSearch(
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
        whenever(getPhotoListUseCase(testQuery)).thenReturn(
            flowOf(
                PhotoSearchResult.Success(
                    fakePhotoList
                )
            )
        )

        // when
        subject.setEvent(MainUiEvent.RequestSearch(testQuery, BottomBarScreen.Search))

        // then
        subject.viewState.value.lastSearch shouldBe testQuery
    }

    @Test
    fun `OnSearchQueryChange should update the searchQuery string in the viewState`() = runTest {
        // when
        subject.setEvent(MainUiEvent.OnSearchQueryChange(testQuery))
        // then
        subject.viewState.value.searchQuery shouldBe testQuery

        // when
        subject.setEvent(MainUiEvent.OnSearchQueryChange(anotherTestQuery))
        // then
        subject.viewState.value.searchQuery shouldBe anotherTestQuery
    }

    @Test
    fun `RemoveSearchHistory should delete the saved query from search history`() = runTest {
        whenever(getPhotoListUseCase(testQuery)).thenReturn(
            flowOf(
                PhotoSearchResult.Success(
                    fakePhotoList
                )
            )
        )

        whenever(getPhotoListUseCase(anotherTestQuery)).thenReturn(
            flowOf(
                PhotoSearchResult.Success(
                    fakePhotoList
                )
            )
        )

        // when
        subject.setEvent(MainUiEvent.RequestSearch(testQuery, BottomBarScreen.History))
        subject.setEvent(MainUiEvent.RequestSearch(anotherTestQuery, BottomBarScreen.History))
        subject.viewState.value.searchHistory shouldBe ArrayDeque(
            listOf(
                anotherTestQuery,
                testQuery
            )
        )
        subject.setEvent(MainUiEvent.RemoveSearchHistory(0))

        // then
        subject.viewState.value.searchHistory shouldBe ArrayDeque(listOf(testQuery))
    }

    @Test
    fun `OnSearchHistoryItemSelected should switch to Search screen if fromScreen is Home`() =
        runTest {
            whenever(getPhotoListUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Success(
                        fakePhotoList
                    )
                )
            )

            // when
            subject.setEvent(
                MainUiEvent.OnSearchHistoryItemSelected(
                    testQuery,
                    BottomBarScreen.Home
                )
            )

            // then
            verify(getPhotoListUseCase).invoke(testQuery)

            subject.effect.test {
                awaitItem() shouldBe MainUiEffect.Navigation.SwitchScreen(BottomBarScreen.Search)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `OnSearchHistoryItemSelected should switch to Search screen if fromScreen is History`() =
        runTest {
            whenever(getPhotoListUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Success(
                        fakePhotoList
                    )
                )
            )

            // when
            subject.setEvent(
                MainUiEvent.OnSearchHistoryItemSelected(
                    testQuery,
                    BottomBarScreen.History
                )
            )

            // then
            verify(getPhotoListUseCase).invoke(testQuery)

            subject.effect.test {
                awaitItem() shouldBe MainUiEffect.Navigation.SwitchScreen(BottomBarScreen.Search)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `OnSearchHistoryItemSelected from Search screen should not switch screen`() = runTest {
        whenever(getPhotoListUseCase(testQuery)).thenReturn(
            flowOf(
                PhotoSearchResult.Success(
                    fakePhotoList
                )
            )
        )

        // when
        subject.setEvent(MainUiEvent.OnSearchHistoryItemSelected(testQuery, BottomBarScreen.Search))

        // then
        verify(getPhotoListUseCase).invoke(testQuery)

        subject.effect.test {
            expectNoEvents()
        }
    }

    @Test
    fun `OnSearchHistoryItemSelected should trigger search if called triggered from Home`() =
        runTest {
            whenever(getPhotoListUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Success(
                        fakePhotoList
                    )
                )
            )

            // when
            subject.setEvent(
                MainUiEvent.OnSearchHistoryItemSelected(
                    testQuery,
                    BottomBarScreen.Home
                )
            )

            // then
            verify(getPhotoListUseCase).invoke(testQuery)
        }

    @Test
    fun `OnSearchHistoryItemSelected should trigger search if called triggered from Search`() =
        runTest {
            whenever(getPhotoListUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Success(
                        fakePhotoList
                    )
                )
            )

            // when
            subject.setEvent(
                MainUiEvent.OnSearchHistoryItemSelected(
                    testQuery,
                    BottomBarScreen.Search
                )
            )

            // then
            verify(getPhotoListUseCase).invoke(testQuery)
        }

    @Test
    fun `OnSearchHistoryItemSelected should trigger search if called triggered from History`() =
        runTest {
            whenever(getPhotoListUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Success(
                        fakePhotoList
                    )
                )
            )

            // when
            subject.setEvent(
                MainUiEvent.OnSearchHistoryItemSelected(
                    testQuery,
                    BottomBarScreen.History
                )
            )

            // then
            verify(getPhotoListUseCase).invoke(testQuery)
        }

    @Test
    fun `Error config in viewState should have relevant updates when photo list is empty in the result when OnSearchRequest triggers search`() =
        runTest {
            whenever(getPhotoListUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Success(
                        emptyList()
                    )
                )
            )

            // when
            subject.setEvent(
                MainUiEvent.RequestSearch(
                    searchQuery = testQuery,
                    fromScreen = BottomBarScreen.Search
                )
            )

            // then
            subject.viewState.value.lastSearch shouldBe testQuery
            subject.viewState.value.photoList shouldBe emptyList<Photo>()
            subject.viewState.value.isLoading shouldBe false
            subject.viewState.value.searchHistory shouldBe ArrayDeque(listOf(testQuery))

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
            whenever(getPhotoListUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Success(
                        fakePhotoList
                    )
                )
            )

            // when
            subject.setEvent(
                MainUiEvent.RequestSearch(
                    searchQuery = testQuery,
                    fromScreen = BottomBarScreen.Search
                )
            )

            // then
            subject.viewState.value.lastSearch shouldBe testQuery
            subject.viewState.value.photoList shouldBe fakePhotoList
            subject.viewState.value.error shouldBe null
            subject.viewState.value.isLoading shouldBe false
            subject.viewState.value.searchResultTitleRes shouldBe R.string.main_view_model_success_result_title
            subject.viewState.value.searchHistory shouldBe ArrayDeque(listOf(testQuery))
        }

    @Test
    fun `Error config in viewState should have relevant updates when use case returns generic error`() =
        runTest {

            whenever(getPhotoListUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Error.Generic
                )
            )

            // when
            subject.setEvent(
                MainUiEvent.RequestSearch(
                    searchQuery = testQuery,
                    fromScreen = BottomBarScreen.Search
                )
            )

            // then
            subject.viewState.value.lastSearch shouldBe testQuery
            subject.viewState.value.photoList shouldBe emptyList()
            subject.viewState.value.isLoading shouldBe false
            subject.viewState.value.searchHistory shouldBe ArrayDeque(listOf(testQuery))

            subject.viewState.value.error?.let { errorConfig ->
                errorConfig.errorTitleRes shouldBe ContentErrorConfig.ErrorMessage.Resource(R.string.main_view_model_generic_error_title)
                errorConfig.errorSubTitleRes shouldBe ContentErrorConfig.ErrorMessage.Resource(R.string.main_view_model_generic_error_sub_title)

                val capturedRetryFunction = errorConfig.onRetry

                capturedRetryFunction shouldNotBe null

                capturedRetryFunction?.invoke()

                subject.viewState.value.lastSearch shouldBe testQuery
                subject.viewState.value.searchHistory shouldBe ArrayDeque(
                    listOf(
                        testQuery,
                        testQuery
                    )
                )
            }
        }

    @Test
    fun `Error config in viewState should have relevant updates when use case returns no result error`() =
        runTest {

            whenever(getPhotoListUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Error.SearchFailed("search failed")
                )
            )

            // when
            subject.setEvent(
                MainUiEvent.RequestSearch(
                    searchQuery = testQuery,
                    fromScreen = BottomBarScreen.Search
                )
            )

            // then
            subject.viewState.value.lastSearch shouldBe testQuery
            subject.viewState.value.photoList shouldBe emptyList()
            subject.viewState.value.isLoading shouldBe false
            subject.viewState.value.searchHistory shouldBe ArrayDeque(listOf(testQuery))

            subject.viewState.value.error?.let { errorConfig ->
                errorConfig.errorTitleRes shouldBe ContentErrorConfig.ErrorMessage.Resource(R.string.main_view_model_search_failed_error_title)
                errorConfig.errorSubTitleRes shouldBe ContentErrorConfig.ErrorMessage.Text("search failed")

                val capturedRetryFunction = errorConfig.onRetry

                capturedRetryFunction shouldNotBe null

                capturedRetryFunction?.invoke()

                subject.viewState.value.lastSearch shouldBe testQuery
                subject.viewState.value.searchHistory shouldBe ArrayDeque(
                    listOf(
                        testQuery,
                        testQuery
                    )
                )
            }
        }


    @Test
    fun `Error config in viewState should have relevant updates when use case returns no internet connection error`() =
        runTest {

            whenever(getPhotoListUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Error.NoInternetConnection
                )
            )

            // when
            subject.setEvent(
                MainUiEvent.RequestSearch(
                    searchQuery = testQuery,
                    fromScreen = BottomBarScreen.Search
                )
            )

            // then
            subject.viewState.value.lastSearch shouldBe testQuery
            subject.viewState.value.photoList shouldBe emptyList()
            subject.viewState.value.isLoading shouldBe false
            subject.viewState.value.searchHistory shouldBe ArrayDeque(listOf(testQuery))

            subject.viewState.value.error?.let { errorConfig ->
                errorConfig.errorTitleRes shouldBe ContentErrorConfig.ErrorMessage.Resource(R.string.main_view_model_no_internet_connection_error_title)
                errorConfig.errorSubTitleRes shouldBe ContentErrorConfig.ErrorMessage.Resource(R.string.main_view_model_no_internet_connection_error_sub_title)

                val capturedRetryFunction = errorConfig.onRetry

                capturedRetryFunction shouldNotBe null

                capturedRetryFunction?.invoke()

                subject.viewState.value.lastSearch shouldBe testQuery
                subject.viewState.value.searchHistory shouldBe ArrayDeque(
                    listOf(
                        testQuery,
                        testQuery
                    )
                )
            }
        }

    @Test
    fun `viewState should have empty photo list when use case returns generic error`() =
        runTest {
            whenever(getPhotoListUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Success(
                        fakePhotoList
                    )
                )
            )

            // when
            subject.setEvent(
                MainUiEvent.RequestSearch(
                    searchQuery = testQuery,
                    fromScreen = BottomBarScreen.Search
                )
            )

            // then

            subject.viewState.value.photoList shouldBe fakePhotoList

            whenever(getPhotoListUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Error.Generic
                )
            )

            // when
            subject.setEvent(
                MainUiEvent.RequestSearch(
                    searchQuery = testQuery,
                    fromScreen = BottomBarScreen.Search
                )
            )
            subject.viewState.value.photoList shouldBe emptyList()
        }

    @Test
    fun `viewState should have empty photo list when use case returns NoInternetConnection error`() =
        runTest {
            whenever(getPhotoListUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Success(
                        fakePhotoList
                    )
                )
            )

            // when
            subject.setEvent(
                MainUiEvent.RequestSearch(
                    searchQuery = testQuery,
                    fromScreen = BottomBarScreen.Search
                )
            )

            // then

            subject.viewState.value.photoList shouldBe fakePhotoList

            whenever(getPhotoListUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Error.NoInternetConnection
                )
            )

            // when
            subject.setEvent(
                MainUiEvent.RequestSearch(
                    searchQuery = testQuery,
                    fromScreen = BottomBarScreen.Search
                )
            )
            subject.viewState.value.photoList shouldBe emptyList()
        }

    @Test
    fun `viewState should have empty photo list when use case returns SearchFailed error`() =
        runTest {
            whenever(getPhotoListUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Success(
                        fakePhotoList
                    )
                )
            )

            // when
            subject.setEvent(
                MainUiEvent.RequestSearch(
                    searchQuery = testQuery,
                    fromScreen = BottomBarScreen.Search
                )
            )

            // then

            subject.viewState.value.photoList shouldBe fakePhotoList

            whenever(getPhotoListUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Error.SearchFailed("")
                )
            )

            // when
            subject.setEvent(
                MainUiEvent.RequestSearch(
                    searchQuery = testQuery,
                    fromScreen = BottomBarScreen.Search
                )
            )
            subject.viewState.value.photoList shouldBe emptyList()
        }

    @Test
    fun `viewState should have updated search history whenever OnSearchRequest triggers search`() =
        runTest {
            whenever(getPhotoListUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Success(
                        fakePhotoList
                    )
                )
            )

            whenever(getPhotoListUseCase(anotherTestQuery)).thenReturn(
                flowOf(PhotoSearchResult.Success(fakePhotoList))
            )

            // when
            subject.setEvent(
                MainUiEvent.RequestSearch(
                    searchQuery = testQuery,
                    fromScreen = BottomBarScreen.Search
                )
            )
            subject.setEvent(MainUiEvent.RequestSearch(anotherTestQuery, BottomBarScreen.Search))

            // then
            subject.viewState.value.searchHistory shouldBe ArrayDeque(
                listOf(
                    anotherTestQuery,
                    testQuery
                )
            )
        }
}