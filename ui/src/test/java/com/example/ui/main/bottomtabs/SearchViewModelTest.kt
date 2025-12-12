package com.example.ui.main.bottomtabs


import app.cash.turbine.test
import com.example.testfeature.rule.CoroutineTestRule
import com.example.ui.util.fakePhoto
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class SearchViewModelTest {
    @get:Rule
    val mainDispatcherRule = CoroutineTestRule()

    private val subject: SearchViewModel by lazy { SearchViewModel() }

    @Test
    fun `Initial state should be set correctly`() = runTest {
        subject.viewState.value.selectedPhoto shouldBe null
    }

    @Test
    fun `OnPhotoClick event should trigger ShowPhotoOverlay effect`() = runTest {
        // when
        subject.setAction(SearchUiAction.OnPhotoClick(fakePhoto))

        // then
        subject.effect.test {
            awaitItem() shouldBe SearchUiEffect.ShowPhotoOverlay
            cancelAndIgnoreRemainingEvents()

            subject.viewState.value.selectedPhoto shouldBe fakePhoto
        }
    }

    @Test
    fun `ClearPhotoOverlay event should trigger HidePhotoOverlay effect`() = runTest {
        // when
        subject.setAction(SearchUiAction.ClearPhotoOverlay)

        // then
        subject.effect.test {
            awaitItem() shouldBe SearchUiEffect.HidePhotoOverlay
            cancelAndIgnoreRemainingEvents()

            subject.viewState.value.selectedPhoto shouldBe null
        }
    }
}