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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.domain.model.PhotoItem
import com.example.ui.R
import com.example.ui.common.SPACING_LARGE
import com.example.ui.common.SPACING_MEDIUM
import com.example.ui.common.content.ContentScreen
import com.example.ui.common.content.ContentTitle
import com.example.ui.common.theme.SapFlickrExampleTheme
import com.example.ui.main.MainUiEvent
import com.example.ui.main.MainViewModel
import com.example.ui.main.MainViewState
import com.example.ui.main.bottomtabs.screen.config.BottomBarScreen
import com.example.ui.main.bottomtabs.view.SearchFieldView

@Composable
internal fun HomeScreen(
    viewModel: MainViewModel,
    viewState: MainViewState
) {
    ContentScreen(
        isLoading = viewState.isLoading,
        backPressHandler = { viewModel.setEvent(MainUiEvent.OnBackPressed(BottomBarScreen.Home)) }
    ) { paddingValues ->
        Content(
            state = viewState,
            onEventSend = { viewModel.setEvent(it) },
            paddingValues = paddingValues,
        )
    }
}

@Composable
private fun Content(
    state: MainViewState,
    onEventSend: (MainUiEvent) -> Unit,
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
            searchInputPrefilledText = state.searchQuery,
            searchHistory = state.searchHistory,
            label = stringResource(R.string.home_screen_search_field_label),
            buttonText = stringResource(R.string.home_screen_search_button_text),
            searchErrorReceived = state.error != null,
            doOnSearchRequest = { text ->
                onEventSend(
                    MainUiEvent.OnSearchRequest(
                        searchQuery = text,
                        BottomBarScreen.Home
                    )
                )
            },
            doOnSearchHistoryDropDownItemClick = { text ->
                onEventSend(
                    MainUiEvent.OnSearchHistoryItemClicked(
                        searchQuery = text,
                        BottomBarScreen.Home
                    )
                )
            },
            doOnSearchTextChange = { text -> onEventSend(MainUiEvent.OnSearchTextChange(text)) }
        )
    }
}

@Preview(showBackground = true)
@PreviewLightDark
@Composable
private fun HomePreview(
    @PreviewParameter(HomePreviewParameterProvider::class) viewState: MainViewState
) {
    SapFlickrExampleTheme {
        ContentScreen(
            isLoading = viewState.isLoading,
        ) {
            Content(
                state = viewState,
                onEventSend = {},
                paddingValues = PaddingValues(1.dp)
            )
        }
    }
}


private class HomePreviewParameterProvider : PreviewParameterProvider<MainViewState> {
    override val values = sequenceOf(
        MainViewState(),
        MainViewState(
            isLoading = false,
            error = null,
            searchQuery = "query",
            photoList = listOf(
                PhotoItem(
                    title = "Photo One",
                    url = "url",
                    isPublic = true,
                    isFriend = true,
                    isFamily = true
                ),
                PhotoItem(
                    title = "Photo Two",
                    url = "url",
                    isPublic = true,
                    isFriend = true,
                    isFamily = true
                )
            ),
            searchHistory = ArrayDeque()
        ),
    )
}
