package com.example.ui.main.bottomtabs.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.domain.model.PhotoItem
import com.example.ui.common.SPACING_MEDIUM
import com.example.ui.common.content.ContentError
import com.example.ui.common.content.ContentScreen
import com.example.ui.common.content.ContentTitle
import com.example.ui.common.keyboardAsState
import com.example.ui.main.MainUiEvent
import com.example.ui.main.MainViewModel
import com.example.ui.main.MainViewState
import com.example.ui.main.bottomtabs.screen.config.BottomBarScreen
import com.example.ui.main.bottomtabs.view.ListView
import com.example.ui.main.bottomtabs.view.SearchFieldView
import kotlinx.coroutines.delay

@Composable
internal fun SearchScreen(viewModel: MainViewModel, viewState: MainViewState) {
    ContentScreen(
        isLoading = viewState.isLoading,
        backPressHandler = { viewModel.setEvent(MainUiEvent.OnBackPressed(BottomBarScreen.Search)) }
    ) { paddingValues ->
        Content(
            state = viewState,
            onEventSend = { viewModel.setEvent(it) },
            paddingValues = paddingValues
        )
    }
}

@Composable
private fun Content(
    state: MainViewState,
    onEventSend: (MainUiEvent) -> Unit,
    paddingValues: PaddingValues
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
        val photosResultAvailable = state.photoList.isNotEmpty()
        if (!photosResultAvailable) {
            ContentTitle(
                modifier = Modifier
                    .fillMaxWidth(),
                title = state.title,
                subtitle = state.subtitle
            )
        }
        val prefilledText =
            if (photosResultAvailable) state.searchHistory.first() else state.searchQuery
        val shouldShowButton = !photosResultAvailable
        SearchFieldView(
            searchInputPrefilledText = prefilledText,
            searchHistory = state.searchHistory,
            label = "Search a photo",
            shouldHaveFocus = !photosResultAvailable,
            buttonText = if (shouldShowButton && state.error == null) "Search" else null,
            searchErrorReceived = state.error != null,
            doOnSearchRequest = { text ->
                onEventSend(
                    MainUiEvent.OnSearchRequest(
                        searchQuery = text,
                        BottomBarScreen.Search
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

        if (photosResultAvailable) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = SPACING_MEDIUM.dp),
                text = state.searchResultTitle,
                overflow = TextOverflow.Ellipsis
            )

            ListView(
                items = state.photoList,
                paddingValues = paddingValues,
                onItemClick = onEventSend
            )
        } else if (state.error != null) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(
                        paddingValues = PaddingValues(
                            top = SPACING_MEDIUM.dp,
                            bottom = paddingValues.calculateBottomPadding()
                        )
                    ),

                verticalArrangement = Arrangement.Center
            ) {
                ContentError(
                    contentErrorConfig = state.error
                )
            }
        } else {
            val isKeyboardOpen by keyboardAsState()
            LaunchedEffect(isKeyboardOpen) {
                delay(300)
                if (!isKeyboardOpen) {
                    onEventSend(MainUiEvent.OnBackPressed(BottomBarScreen.Search))
                }
            }
        }
    }
}

@Composable
@Preview
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

    Content(
        paddingValues = PaddingValues(1.dp),
        state = viewState,
        onEventSend = {}
    )
}