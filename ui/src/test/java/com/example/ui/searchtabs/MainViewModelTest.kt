package com.example.ui.searchtabs

import app.cash.turbine.test
import com.example.domain.PhotoSearchResult
import com.example.domain.model.PhotoItem
import com.example.domain.usecase.GetPhotoListUseCase
import com.example.testfeature.rule.CoroutineTestRule
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
        subject.viewState.value.title shouldBe "Flickr Photo Search"
    }

    @Test
    fun `OnBackPressed event from Home should trigger navigation Pop effect`() = runTest {
        // when
        subject.setEvent(MainUiEvent.OnBackPressed(BottomBarScreen.Home))

        // then
        subject.effect.test {
            awaitItem() shouldBe MainUiEffect.Navigation.Pop(BottomBarScreen.Home)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `OnBackPressed event from Search should trigger navigation Pop effect`() = runTest {
        // when
        subject.setEvent(MainUiEvent.OnBackPressed(BottomBarScreen.Search))

        // then
        subject.effect.test {
            awaitItem() shouldBe MainUiEffect.Navigation.Pop(BottomBarScreen.Search)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `OnBackPressed event from History should trigger navigation Pop effect`() = runTest {
        // when
        subject.setEvent(MainUiEvent.OnBackPressed(BottomBarScreen.History))

        // then
        subject.effect.test {
            awaitItem() shouldBe MainUiEffect.Navigation.Pop(BottomBarScreen.History)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `OnClearAllButtonClick should trigger clear search history`() = runTest {

        whenever(getPhotoListUseCase(testQuery)).thenReturn(
            flowOf(
                PhotoSearchResult.Success(
                    fakePhotoList
                )
            )
        )
        // when
        subject.setEvent(MainUiEvent.OnSearchRequest(testQuery, BottomBarScreen.History))
        // then
        subject.viewState.value.searchHistory shouldBe ArrayDeque(listOf(testQuery))

        // when
        subject.setEvent(MainUiEvent.OnClearAllButtonClick)
        // then
        subject.viewState.value.searchHistory shouldBe ArrayDeque()
    }

    @Test
    fun `OnSearchRequest should trigger search and switch to Search screen if fromScreen is Home`() =
        runTest {
            whenever(getPhotoListUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Success(
                        fakePhotoList
                    )
                )
            )
            // when
            subject.setEvent(MainUiEvent.OnSearchRequest(testQuery, BottomBarScreen.Home))

            // then
            verify(getPhotoListUseCase).invoke(testQuery)

            subject.effect.test {
                awaitItem() shouldBe MainUiEffect.Navigation.SwitchScreen(BottomBarScreen.Search)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `OnSearchRequest should trigger search and switch to Search screen if fromScreen is History`() =
        runTest {
            whenever(getPhotoListUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Success(
                        fakePhotoList
                    )
                )
            )
            // when
            subject.setEvent(MainUiEvent.OnSearchRequest(testQuery, BottomBarScreen.History))

            // then
            verify(getPhotoListUseCase).invoke(testQuery)

            subject.effect.test {
                awaitItem() shouldBe MainUiEffect.Navigation.SwitchScreen(BottomBarScreen.Search)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `OnSearchRequest from Search screen should trigger search but should not switch screen`() =
        runTest {
            whenever(getPhotoListUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Success(
                        fakePhotoList
                    )
                )
            )

            // when
            subject.setEvent(MainUiEvent.OnSearchRequest(testQuery, BottomBarScreen.Search))

            // then
            verify(getPhotoListUseCase).invoke(testQuery)

            subject.effect.test {
                expectNoEvents()
            }
        }

    @Test
    fun `OnSearchTextChange should update the searchQuery string in the viewState`() = runTest {
        // when
        subject.setEvent(MainUiEvent.OnSearchTextChange(testQuery))
        // then
        subject.viewState.value.searchQuery shouldBe testQuery

        // when
        subject.setEvent(MainUiEvent.OnSearchTextChange(anotherTestQuery))
        // then
        subject.viewState.value.searchQuery shouldBe anotherTestQuery
    }

    @Test
    fun `DeleteFromSearchHistory should delete item from search history`() = runTest {
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
        subject.setEvent(MainUiEvent.OnSearchRequest(testQuery, BottomBarScreen.History))
        subject.setEvent(MainUiEvent.OnSearchRequest(anotherTestQuery, BottomBarScreen.History))
        subject.viewState.value.searchHistory shouldBe ArrayDeque(
            listOf(
                anotherTestQuery,
                testQuery
            )
        )
        subject.setEvent(MainUiEvent.DeleteFromSearchHistory(0))

        // then
        subject.viewState.value.searchHistory shouldBe ArrayDeque(listOf(testQuery))
    }

    @Test
    fun `OnSearchHistoryItemClicked should switch to Search screen if fromScreen is Home`() =
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
                MainUiEvent.OnSearchHistoryItemClicked(
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
    fun `OnSearchHistoryItemClicked should switch to Search screen if fromScreen is History`() =
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
                MainUiEvent.OnSearchHistoryItemClicked(
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
    fun `OnSearchHistoryItemClicked from Search screen should not switch screen`() = runTest {
        whenever(getPhotoListUseCase(testQuery)).thenReturn(
            flowOf(
                PhotoSearchResult.Success(
                    fakePhotoList
                )
            )
        )

        // when
        subject.setEvent(MainUiEvent.OnSearchHistoryItemClicked(testQuery, BottomBarScreen.Search))

        // then
        verify(getPhotoListUseCase).invoke(testQuery)

        subject.effect.test {
            expectNoEvents()
        }
    }

    @Test
    fun `OnSearchHistoryItemClicked should trigger search if called triggered from Home`() =
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
                MainUiEvent.OnSearchHistoryItemClicked(
                    testQuery,
                    BottomBarScreen.Home
                )
            )

            // then
            verify(getPhotoListUseCase).invoke(testQuery)
        }

    @Test
    fun `OnSearchHistoryItemClicked should trigger search if called triggered from Search`() =
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
                MainUiEvent.OnSearchHistoryItemClicked(
                    testQuery,
                    BottomBarScreen.Search
                )
            )

            // then
            verify(getPhotoListUseCase).invoke(testQuery)
        }

    @Test
    fun `OnSearchHistoryItemClicked should trigger search if called triggered from History`() =
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
                MainUiEvent.OnSearchHistoryItemClicked(
                    testQuery,
                    BottomBarScreen.History
                )
            )

            // then
            verify(getPhotoListUseCase).invoke(testQuery)
        }

    @Test
    fun `viewState should have relevant updates when photo list is empty in the result when OnSearchRequest triggers search`() =
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
                MainUiEvent.OnSearchRequest(
                    searchQuery = testQuery,
                    fromScreen = BottomBarScreen.Search
                )
            )

            // then
            subject.viewState.value.searchQuery shouldBe testQuery
            subject.viewState.value.photoList shouldBe emptyList<PhotoItem>()
            subject.viewState.value.isLoading shouldBe false
            subject.viewState.value.searchHistory shouldBe ArrayDeque(listOf(testQuery))

            subject.viewState.value.error?.let { errorConfig ->
                errorConfig.errorTitle shouldBe "Nothing found..."
                errorConfig.errorSubTitle shouldBe "Unable to find anything. Try something else"

                val capturedRetryFunction = errorConfig.onRetry

                capturedRetryFunction shouldNotBe null

                capturedRetryFunction?.invoke()

                subject.viewState.value.searchQuery shouldBe testQuery
            }
        }

    @Test
    fun `viewState should have photo list when use case returns success with populated photo list`() =
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
                MainUiEvent.OnSearchRequest(
                    searchQuery = testQuery,
                    fromScreen = BottomBarScreen.Search
                )
            )

            // then
            subject.viewState.value.searchQuery shouldBe testQuery
            subject.viewState.value.photoList shouldBe fakePhotoList
            subject.viewState.value.error shouldBe null
            subject.viewState.value.isLoading shouldBe false
            subject.viewState.value.searchResultTitle shouldBe "Showing results for \"$testQuery\""
            subject.viewState.value.searchHistory shouldBe ArrayDeque(listOf(testQuery))
        }

    @Test
    fun `viewState should have relevant updates when use case returns generic error`() =
        runTest {

            whenever(getPhotoListUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Error.Generic
                )
            )

            // when
            subject.setEvent(
                MainUiEvent.OnSearchRequest(
                    searchQuery = testQuery,
                    fromScreen = BottomBarScreen.Search
                )
            )

            // then
            subject.viewState.value.searchQuery shouldBe testQuery
            subject.viewState.value.photoList shouldBe emptyList()
            subject.viewState.value.isLoading shouldBe false
            subject.viewState.value.subtitle shouldBe ""
            subject.viewState.value.searchHistory shouldBe ArrayDeque(listOf(testQuery))

            subject.viewState.value.error?.let { errorConfig ->
                errorConfig.errorTitle shouldBe "Oops..."
                errorConfig.errorSubTitle shouldBe "Something went wrong."

                val capturedRetryFunction = errorConfig.onRetry

                capturedRetryFunction shouldNotBe null

                capturedRetryFunction?.invoke()

                subject.viewState.value.searchQuery shouldBe testQuery
            }
        }

    @Test
    fun `viewState should have relevant updates when use case returns no result error`() =
        runTest {

            whenever(getPhotoListUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Error.NoResult
                )
            )

            // when
            subject.setEvent(
                MainUiEvent.OnSearchRequest(
                    searchQuery = testQuery,
                    fromScreen = BottomBarScreen.Search
                )
            )

            // then
            subject.viewState.value.searchQuery shouldBe testQuery
            subject.viewState.value.photoList shouldBe emptyList()
            subject.viewState.value.isLoading shouldBe false
            subject.viewState.value.subtitle shouldBe ""
            subject.viewState.value.searchHistory shouldBe ArrayDeque(listOf(testQuery))

            subject.viewState.value.error?.let { errorConfig ->
                errorConfig.errorTitle shouldBe "Oops..."
                errorConfig.errorSubTitle shouldBe "Result Unavailable."

                val capturedRetryFunction = errorConfig.onRetry

                capturedRetryFunction shouldNotBe null

                capturedRetryFunction?.invoke()

                subject.viewState.value.searchQuery shouldBe testQuery
            }
        }


    @Test
    fun `viewState should have relevant updates when use case returns no internet connection error`() =
        runTest {

            whenever(getPhotoListUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Error.NoInternetConnection
                )
            )

            // when
            subject.setEvent(
                MainUiEvent.OnSearchRequest(
                    searchQuery = testQuery,
                    fromScreen = BottomBarScreen.Search
                )
            )

            // then
            subject.viewState.value.searchQuery shouldBe testQuery
            subject.viewState.value.photoList shouldBe emptyList()
            subject.viewState.value.isLoading shouldBe false
            subject.viewState.value.subtitle shouldBe ""
            subject.viewState.value.searchHistory shouldBe ArrayDeque(listOf(testQuery))

            subject.viewState.value.error?.let { errorConfig ->
                errorConfig.errorTitle shouldBe "Oops..."
                errorConfig.errorSubTitle shouldBe "Please check your Internet connection."

                val capturedRetryFunction = errorConfig.onRetry

                capturedRetryFunction shouldNotBe null

                capturedRetryFunction?.invoke()

                subject.viewState.value.searchQuery shouldBe testQuery
            }
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
                MainUiEvent.OnSearchRequest(
                    searchQuery = testQuery,
                    fromScreen = BottomBarScreen.Search
                )
            )
            subject.setEvent(MainUiEvent.OnSearchRequest(anotherTestQuery, BottomBarScreen.Search))

            // then
            subject.viewState.value.searchHistory shouldBe ArrayDeque(
                listOf(
                    anotherTestQuery,
                    testQuery
                )
            )
        }
}