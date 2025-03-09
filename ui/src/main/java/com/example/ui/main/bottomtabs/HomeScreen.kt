package com.example.ui.main.bottomtabs

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.domain.model.PhotoItem
import com.example.ui.common.content.ContentScreen
import com.example.ui.common.content.ContentTitle
import com.example.ui.main.MainUiEvent
import com.example.ui.main.MainViewModel
import com.example.ui.main.MainViewState
import com.example.ui.main.bottomtabs.screen.BottomBarScreen
import com.example.ui.main.bottomtabs.view.SearchInputField

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
                    top = paddingValues.calculateTopPadding(),
                    bottom = 0.dp,
                    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr)
                )
            )
    ) {
        ContentTitle(
            modifier = Modifier
                .fillMaxWidth(),
            title = state.title,
            subtitle = state.subtitle
        )

        SearchInputField(
            searchInputPrefilledText = state.searchQuery,
            searchHistory = state.searchHistory,
            label = "Search a photo",
            buttonText = "Search",
            searchErrorReceived = state.error != null,
            doOnSearchRequest = { text ->
                onEventSend(
                    MainUiEvent.OnSearchRequest(
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
@Composable
private fun SearchScreenPreview() {
    val viewState = MainViewState(
        isLoading = false,
        error = null,
        title = "Flickr Photo Search",
        subtitle = "",
        searchQuery = "",
        searchResultTitle = "Showing results for This",
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
    )
    SearchScreenPreviewWithViewState(viewState)

}

@Composable
private fun SearchScreenPreviewWithViewState(viewState: MainViewState) {
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


