package com.example.ui.searchtabs

import app.cash.turbine.test
import com.example.domain.PhotoSearchResult
import com.example.domain.model.PhotoItem
import com.example.domain.usecase.GetPhotoListUseCase
import com.example.testfeature.rule.CoroutineTestRule
import com.example.ui.main.MainUiEffect
import com.example.ui.main.MainUiEvent
import com.example.ui.main.MainViewModel
import com.example.ui.main.bottomtabs.screen.BottomBarScreen
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
        subject.setEvent(MainUiEvent.OnBackPressed(BottomBarScreen.Home))
        subject.effect.test {
            awaitItem() shouldBe MainUiEffect.Navigation.Pop(BottomBarScreen.Home)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `OnBackPressed event from Search should trigger navigation Pop effect`() = runTest {
        subject.setEvent(MainUiEvent.OnBackPressed(BottomBarScreen.Search))
        subject.effect.test {
            awaitItem() shouldBe MainUiEffect.Navigation.Pop(BottomBarScreen.Search)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `OnBackPressed event from History should trigger navigation Pop effect`() = runTest {
        subject.setEvent(MainUiEvent.OnBackPressed(BottomBarScreen.History))
        subject.effect.test {
            awaitItem() shouldBe MainUiEffect.Navigation.Pop(BottomBarScreen.History)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `OnSearchRequest should trigger search`() = runTest {
        whenever(getPhotoListUseCase(testQuery)).thenReturn(
            flowOf(
                PhotoSearchResult.Success(
                    emptyList()
                )
            )
        )

        subject.setEvent(MainUiEvent.OnSearchRequest(testQuery))

        verify(getPhotoListUseCase).invoke(testQuery)
    }

    @Test
    fun `OnClearAllButtonClick should trigger search`() = runTest {
        whenever(getPhotoListUseCase(testQuery)).thenReturn(
            flowOf(
                PhotoSearchResult.Success(
                    fakePhotoList
                )
            )
        )

        subject.setEvent(MainUiEvent.OnSearchRequest(testQuery))

        subject.viewState.value.searchHistory shouldBe ArrayDeque(listOf(testQuery))

        subject.setEvent(MainUiEvent.OnClearAllButtonClick)

        subject.viewState.value.searchHistory shouldBe ArrayDeque()
    }

    @Test
    fun `DeleteFromSearchHistory should trigger search`() = runTest {
        whenever(getPhotoListUseCase(testQuery)).thenReturn(
            flowOf(
                PhotoSearchResult.Success(
                    emptyList()
                )
            )
        )

        whenever(getPhotoListUseCase(anotherTestQuery)).thenReturn(
            flowOf(
                PhotoSearchResult.Success(
                    emptyList()
                )
            )
        )

        subject.setEvent(MainUiEvent.OnSearchRequest(testQuery))
        subject.setEvent(MainUiEvent.OnSearchRequest(anotherTestQuery))

        subject.viewState.value.searchHistory shouldBe ArrayDeque(
            listOf(
                anotherTestQuery,
                testQuery
            )
        )
        subject.setEvent(MainUiEvent.DeleteFromSearchHistory(0))
        subject.viewState.value.searchHistory shouldBe ArrayDeque(listOf(testQuery))
    }

    @Test
    fun `Viewstate should have relevant updates when photo list is empty in the result when OnSearchRequest triggers search`() =
        runTest {
            whenever(getPhotoListUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Success(
                        emptyList()
                    )
                )
            )

            subject.setEvent(MainUiEvent.OnSearchRequest(testQuery))

            subject.viewState.value.searchQuery shouldBe testQuery
            subject.viewState.value.photoList shouldBe emptyList<PhotoItem>()
            subject.viewState.value.isLoading shouldBe false
            subject.viewState.value.searchHistory shouldBe ArrayDeque(listOf(testQuery))

            subject.viewState.value.error?.let { errorConfig ->
                errorConfig.errorTitle shouldBe "Nothing found..."
                errorConfig.errorSubTitle shouldBe "Unable to find anything. Try something else"
                errorConfig.retryButtonText shouldBe "Try Again"

                val capturedRetryFunction = errorConfig.onRetry

                capturedRetryFunction shouldNotBe null

                capturedRetryFunction?.invoke()

                subject.viewState.value.searchQuery shouldBe testQuery
            }
        }

    @Test
    fun `Viewstate should have photo list when usecase returns success with populated photo list`() =
        runTest {
            whenever(getPhotoListUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Success(
                        fakePhotoList
                    )
                )
            )

            subject.setEvent(MainUiEvent.OnSearchRequest(testQuery))

            subject.viewState.value.searchQuery shouldBe testQuery
            subject.viewState.value.photoList shouldBe fakePhotoList
            subject.viewState.value.error shouldBe null
            subject.viewState.value.isLoading shouldBe false
            subject.viewState.value.searchResultTitle shouldBe "Showing results for \"$testQuery\""
            subject.viewState.value.searchHistory shouldBe ArrayDeque(listOf(testQuery))
        }

    @Test
    fun `Viewstate should have relevant updates when usecase returns error`() =
        runTest {

            whenever(getPhotoListUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Error("error message")
                )
            )

            subject.setEvent(MainUiEvent.OnSearchRequest(testQuery))

            subject.viewState.value.searchQuery shouldBe testQuery
            subject.viewState.value.photoList shouldBe emptyList()
            subject.viewState.value.isLoading shouldBe false
            subject.viewState.value.subtitle shouldBe ""
            subject.viewState.value.searchHistory shouldBe ArrayDeque(listOf(testQuery))

            subject.viewState.value.error?.let { errorConfig ->
                errorConfig.errorTitle shouldBe "Oops..."
                errorConfig.errorSubTitle shouldBe "error message"
                errorConfig.retryButtonText shouldBe "Try Again"

                val capturedRetryFunction = errorConfig.onRetry

                capturedRetryFunction shouldNotBe null

                capturedRetryFunction?.invoke()

                subject.viewState.value.searchQuery shouldBe testQuery
            }
        }

    @Test
    fun `Viewstate should have updated search history whenever OnSearchRequest triggers search`() =
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

            subject.setEvent(MainUiEvent.OnSearchRequest(testQuery))
            subject.setEvent(MainUiEvent.OnSearchRequest(anotherTestQuery))

            subject.viewState.value.searchHistory shouldBe ArrayDeque(
                listOf(
                    anotherTestQuery,
                    testQuery
                )
            )
        }
}