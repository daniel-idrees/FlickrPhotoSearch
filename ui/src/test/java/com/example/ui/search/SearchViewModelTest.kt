package com.example.ui.search

import app.cash.turbine.test
import com.example.domain.PhotoSearchResult
import com.example.domain.model.PhotoItem
import com.example.domain.usecase.GetPhotoListUseCase
import com.example.testfeature.rule.CoroutineTestRule
import com.example.ui.util.anotherTestQuery
import com.example.ui.util.fakePhoto
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
internal class SearchViewModelTest {
    @get:Rule
    val mainDispatcherRule = CoroutineTestRule()

    @Mock
    private lateinit var getPhotoListUseCase: GetPhotoListUseCase

    private val subject: SearchViewModel by lazy {
        SearchViewModel(getPhotoListUseCase)
    }

    @Test
    fun `Initial state should be set correctly`() = runTest {
        subject.viewState.value.title shouldBe "Flickr Search"
    }

    @Test
    fun `OnBackPressed event should trigger navigation Finish effect`() = runTest {
        subject.setEvent(SearchUiEvent.OnBackPressed)
        subject.effect.test {
            awaitItem() shouldBe SearchUiEffect.Navigation.Finish
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `OnItemClicked event should trigger navigation SwitchScreen effect`() = runTest {
        subject.setEvent(SearchUiEvent.OnItemClicked(fakePhoto))
        subject.effect.test {
            awaitItem() shouldBe SearchUiEffect.Navigation.SwitchScreen(screenRoute = "detail")
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

        subject.setEvent(SearchUiEvent.OnSearchRequest(testQuery))

        verify(getPhotoListUseCase).invoke(testQuery)
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

            subject.setEvent(SearchUiEvent.OnSearchRequest(testQuery))

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
    fun `Viewstate should have photo list when usecase returns success with populated photo list`() =
        runTest {
            whenever(getPhotoListUseCase(testQuery)).thenReturn(
                flowOf(
                    PhotoSearchResult.Success(
                        fakePhotoList
                    )
                )
            )

            subject.setEvent(SearchUiEvent.OnSearchRequest(testQuery))

            subject.viewState.value.searchQuery shouldBe testQuery
            subject.viewState.value.photoList shouldBe fakePhotoList
            subject.viewState.value.error shouldBe null
            subject.viewState.value.isLoading shouldBe false
            subject.viewState.value.subtitle shouldBe "Result for \"$testQuery\""
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

            subject.setEvent(SearchUiEvent.OnSearchRequest(testQuery))

            subject.viewState.value.searchQuery shouldBe testQuery
            subject.viewState.value.photoList shouldBe emptyList()
            subject.viewState.value.isLoading shouldBe false
            subject.viewState.value.subtitle shouldBe ""
            subject.viewState.value.searchHistory shouldBe ArrayDeque(listOf(testQuery))

            subject.viewState.value.error?.let { errorConfig ->
                errorConfig.errorTitle shouldBe "Oops..."
                errorConfig.errorSubTitle shouldBe "error message"

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

            subject.setEvent(SearchUiEvent.OnSearchRequest(testQuery))
            subject.setEvent(SearchUiEvent.OnSearchRequest(anotherTestQuery))

            subject.viewState.value.searchHistory shouldBe ArrayDeque(
                listOf(
                    anotherTestQuery,
                    testQuery
                )
            )
        }
}