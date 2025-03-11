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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.domain.model.PhotoItem
import com.example.ui.R
import com.example.ui.common.SPACING_MEDIUM
import com.example.ui.common.content.ContentScreen
import com.example.ui.common.content.ContentTitle
import com.example.ui.common.theme.FlickrPhotoSearchTheme
import com.example.ui.main.MainUiEvent
import com.example.ui.main.MainViewModel
import com.example.ui.main.MainViewState
import com.example.ui.main.bottomtabs.screen.config.BottomBarScreen
import com.example.ui.main.bottomtabs.view.SearchHistoryListView

@Composable
internal fun SearchHistoryScreen(viewModel: MainViewModel, viewState: MainViewState) {
    ContentScreen(
        isLoading = viewState.isLoading,
        backPressHandler = { viewModel.setEvent(MainUiEvent.OnBackPressed(BottomBarScreen.History)) }
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
    paddingValues: PaddingValues,
) {

    val searchHistory = state.searchHistory

    Column(
        verticalArrangement = Arrangement.spacedBy(SPACING_MEDIUM.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(
                paddingValues = PaddingValues(
                    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr)
                )
            )
    ) {
        ContentTitle(
            modifier = Modifier
                .fillMaxWidth(),
            title = stringResource(R.string.search_history_screen_title),
        )

        SearchHistoryListView(
            modifier = Modifier.fillMaxHeight(),
            searchHistory = searchHistory,
            fromScreen = BottomBarScreen.History,
            onEventSend = onEventSend
        )
    }
}

@PreviewLightDark
@Composable
private fun SearchHistoryPreview(
    @PreviewParameter(SearchHistoryPreviewParameterProvider::class) viewState: MainViewState
) {
    FlickrPhotoSearchTheme {
        ContentScreen(
            isLoading = viewState.isLoading,
            backPressHandler = { }
        ) {
            Content(
                state = viewState,
                onEventSend = {},
                paddingValues = PaddingValues(1.dp),
            )
        }
    }
}


private class SearchHistoryPreviewParameterProvider : PreviewParameterProvider<MainViewState> {
    override val values = sequenceOf(
        MainViewState(
            searchQuery = "query",
            searchResultTitleRes = R.string.main_view_model_success_result_title,
            photoList = listOf(
                PhotoItem(
                    title = "Photo One",
                    url = "url",
                    isPublic = false,
                    isFriend = false,
                    isFamily = false
                )
            ),
            searchHistory = ArrayDeque(listOf("test1", "test2"))
        ),
        MainViewState(
            photoList = emptyList(),
            searchHistory = ArrayDeque()
        )
    )
}
