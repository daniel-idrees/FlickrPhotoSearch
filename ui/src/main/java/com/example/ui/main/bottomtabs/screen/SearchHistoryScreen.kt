package com.example.ui.main.bottomtabs.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.PhotoItem
import com.example.ui.R
import com.example.ui.common.SPACING_EXTRA_SMALL
import com.example.ui.common.SPACING_LARGE
import com.example.ui.common.SPACING_MEDIUM
import com.example.ui.common.SPACING_SMALL
import com.example.ui.common.content.ContentScreen
import com.example.ui.common.content.ContentTitle
import com.example.ui.common.theme.SapFlickrExampleTheme
import com.example.ui.main.MainUiEvent
import com.example.ui.main.MainViewModel
import com.example.ui.main.MainViewState
import com.example.ui.main.bottomtabs.screen.config.BottomBarScreen
import com.example.ui.main.bottomtabs.view.DeleteIcon
import com.example.ui.main.bottomtabs.view.PastIcon

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
                    bottom = 0.dp,
                    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr)
                )
            )
    ) {
        val isSearchHistoryAvailable = searchHistory.isNotEmpty()

        ContentTitle(
            modifier = Modifier
                .fillMaxWidth(),
            title = stringResource(R.string.search_history_screen_title),
            subtitle = if (!isSearchHistoryAvailable) stringResource(R.string.search_history_sub_title_if_no_history) else null
        )

        if (isSearchHistoryAvailable) {
            Button(
                modifier = Modifier
                    .padding(horizontal = SPACING_EXTRA_SMALL.dp)
                    .fillMaxWidth(),
                onClick = {
                    onEventSend(MainUiEvent.OnClearAllButtonClick)
                },
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    text = stringResource(R.string.search_history_clear_all_button_text),
                    fontSize = 15.sp,
                )
            }
            SearchHistoryListView(
                modifier = Modifier.fillMaxHeight(),
                searchHistory = searchHistory,
                onEventSend = onEventSend
            )
        }
    }
}

@Composable
private fun SearchHistoryListView(
    modifier: Modifier = Modifier,
    searchHistory: ArrayDeque<String>,
    onEventSend: (MainUiEvent) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(SPACING_MEDIUM.dp),
    ) {

        items(searchHistory.size) { index ->
            SearchHistoryItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = SPACING_SMALL.dp,
                        vertical = SPACING_EXTRA_SMALL.dp
                    ),
                searchText = searchHistory.get(index),
                index = index,
                onEventSend = onEventSend
            )
        }
    }
}

@Composable
private fun SearchHistoryItem(
    modifier: Modifier = Modifier,
    searchText: String, index: Int,
    onEventSend: (MainUiEvent) -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(SPACING_MEDIUM.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier
                .weight(0.90f)
                .clickable {
                    onEventSend(
                        MainUiEvent.OnSearchHistoryItemClicked(
                            searchText,
                            BottomBarScreen.History
                        )
                    )
                },
            shape = RoundedCornerShape(SPACING_SMALL.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = SPACING_EXTRA_SMALL.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = SPACING_SMALL.dp,
                        vertical = SPACING_EXTRA_SMALL.dp
                    ),
                horizontalArrangement = Arrangement.spacedBy(SPACING_MEDIUM.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PastIcon(modifier = Modifier.size(SPACING_LARGE.dp))

                Text(
                    text = searchText,
                )
            }
        }

        DeleteIcon(modifier = Modifier
            .size(SPACING_LARGE.dp)
            .weight(0.10f)
            .clickable {
                onEventSend(MainUiEvent.DeleteFromSearchHistory(index))
            }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@PreviewLightDark
@Composable
private fun SearchHistoryPreview(
    @PreviewParameter(SearchHistoryPreviewParameterProvider::class) viewState: MainViewState
) {
    SapFlickrExampleTheme {
        ContentScreen(
            isLoading = viewState.isLoading,
            backPressHandler = {  }
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
