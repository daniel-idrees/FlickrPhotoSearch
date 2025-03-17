package com.example.ui.main.bottomtabs.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.ui.R
import com.example.ui.common.SPACING_EXTRA_SMALL
import com.example.ui.common.SPACING_MEDIUM
import com.example.ui.common.SPACING_SMALL
import com.example.ui.main.MainUiEvent
import com.example.ui.main.bottomtabs.screen.config.BottomBarScreen

@Composable
internal fun SearchHistoryListView(
    modifier: Modifier = Modifier,
    searchHistory: List<String>,
    fromScreen: BottomBarScreen,
    onEventSend: (MainUiEvent) -> Unit,
) {

    if (searchHistory.isEmpty()) {
        TextBodyMedium(
            modifier.fillMaxWidth(),
            text = stringResource(R.string.search_history_sub_title_if_no_history)
        )
        return
    }
    
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(SPACING_MEDIUM.dp)
    ) {
        ButtonWithTextView(
            modifier = Modifier
                .padding(horizontal = SPACING_EXTRA_SMALL.dp)
                .fillMaxWidth(),
            buttonText = stringResource(R.string.search_history_clear_all_button_text),
            onClick = {
                onEventSend(MainUiEvent.ClearSearchHistory)
            }
        )

        LazyColumn(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(SPACING_MEDIUM.dp),
        ) {

            items(searchHistory.size) { index ->
                val searchText = searchHistory.get(index)
                SearchHistoryItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = SPACING_SMALL.dp,
                            vertical = SPACING_EXTRA_SMALL.dp
                        ),
                    searchText = searchText,
                    onItemClick = {
                        onEventSend(
                            MainUiEvent.OnSearchHistoryItemSelected(
                                searchText,
                                fromScreen
                            )
                        )
                    },
                    onDeleteIconClick = { onEventSend(MainUiEvent.RemoveSearchHistory(index)) }
                )
            }
        }
    }
}

@Composable
@PreviewLightDark
private fun SearchHistoryListViewPreview() {
    SearchHistoryListView(
        searchHistory = ArrayDeque(listOf("text1", "text2")),
        fromScreen = BottomBarScreen.History,
        onEventSend = {}
    )
}

@Composable
@PreviewLightDark
private fun SearchHistoryListViewEmptyPreview() {
    SearchHistoryListView(
        searchHistory = ArrayDeque(),
        fromScreen = BottomBarScreen.History,
        onEventSend = {}
    )
}

