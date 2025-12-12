package com.example.ui.main.bottomtabs.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.model.Photo
import com.example.ui.R
import com.example.ui.common.SPACING_LARGE
import com.example.ui.common.content.ContentScreen
import com.example.ui.common.content.ContentTitle
import com.example.ui.common.theme.FlickrPhotoSearchTheme
import com.example.ui.main.MainUiAction
import com.example.ui.main.MainViewModel
import com.example.ui.main.MainViewState
import com.example.ui.main.bottomtabs.screen.config.BottomBarScreen
import com.example.ui.main.bottomtabs.view.SearchFieldView

@Composable
internal fun HomeScreen(
    viewModel: MainViewModel,
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    ContentScreen(
        isLoading = viewState.isLoading,
        backPressHandler = { viewModel.setAction(MainUiAction.OnNavigateBackRequest(BottomBarScreen.Home)) }
    ) { paddingValues ->
        Content(
            searchQuery = viewState.searchQuery,
            searchHistory = viewState.searchHistory,
            hasError = viewState.error != null,
            onEventSend = { viewModel.setAction(it) },
            paddingValues = paddingValues,
        )
    }
}

@Composable
private fun Content(
    searchQuery: String,
    searchHistory: List<String>,
    hasError: Boolean,
    onEventSend: (MainUiAction) -> Unit,
    paddingValues: PaddingValues,
) {
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .padding(
                paddingValues = PaddingValues(
                    bottom = 0.dp,
                    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr)
                )
            )
    ) {
        ContentTitle(
            modifier = Modifier
                .fillMaxWidth(),
            title = stringResource(R.string.home_screen_title),
        )

        SearchFieldView(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = SPACING_LARGE.dp),
            searchInputPrefilledText = searchQuery,
            searchHistory = searchHistory,
            label = stringResource(R.string.home_screen_search_field_label),
            buttonText = stringResource(R.string.home_screen_search_button_text),
            searchErrorReceived = hasError,
            doOnSearchRequest = { text ->
                onEventSend(
                    MainUiAction.RequestSearch(
                        searchQuery = text,
                        BottomBarScreen.Home
                    )
                )
            },
            doOnSearchHistoryDropDownItemClick = { text ->
                onEventSend(
                    MainUiAction.OnSearchHistoryItemSelected(
                        searchQuery = text,
                        BottomBarScreen.Home
                    )
                )
            },
            doOnSearchTextChange = { text -> onEventSend(MainUiAction.OnSearchQueryChange(text)) },
            doOnClearHistoryClick = { index ->
                onEventSend(MainUiAction.RemoveSearchHistory(index))
            }
        )
    }
}

@PreviewLightDark
@Composable
private fun HomePreview(
    @PreviewParameter(HomePreviewParameterProvider::class) viewState: MainViewState,
) {
    FlickrPhotoSearchTheme {
        ContentScreen(
            isLoading = viewState.isLoading,
        ) {
            Content(
                searchQuery = viewState.searchQuery,
                searchHistory = viewState.searchHistory,
                hasError = viewState.error != null,
                onEventSend = {},
                paddingValues = PaddingValues(1.dp)
            )
        }
    }
}


private class HomePreviewParameterProvider : PreviewParameterProvider<MainViewState> {
    val viewState = MainViewState(
        photoList = emptyList(),
        searchHistory = emptyList(),
        isLoading = false,
        error = null,
        searchQuery = "",
        lastSearch = ""
    )

    override val values = sequenceOf(
        viewState,
        viewState.copy(
            searchQuery = "query",
            photoList = listOf(
                Photo(
                    title = "Photo One",
                    url = "url",
                    isPublic = true,
                    isFriend = true,
                    isFamily = true
                ),
                Photo(
                    title = "Photo Two",
                    url = "url",
                    isPublic = true,
                    isFriend = true,
                    isFamily = true
                )
            ),
        ),
    )
}
