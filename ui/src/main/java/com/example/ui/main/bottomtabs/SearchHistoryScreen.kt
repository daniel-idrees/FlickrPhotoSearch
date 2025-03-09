package com.example.ui.main.bottomtabs

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.PhotoItem
import com.example.ui.main.MainUiEvent
import com.example.ui.main.MainViewModel
import com.example.ui.main.MainViewState
import com.example.ui.R
import com.example.ui.common.SPACING_EXTRA_LARGE
import com.example.ui.common.SPACING_EXTRA_SMALL
import com.example.ui.common.SPACING_LARGE
import com.example.ui.common.SPACING_MEDIUM
import com.example.ui.common.SPACING_SMALL
import com.example.ui.common.content.ContentScreen
import com.example.ui.common.content.ContentTitle
import com.example.ui.main.bottomtabs.screen.BottomBarScreen

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
            title = "Search History",
        )

        if (searchHistory.isEmpty()) {
            Text(text = "No search history")
        } else {
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
                    text = "Clear All",
                    fontSize = 15.sp,
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(SPACING_MEDIUM.dp),
            ) {

                items(searchHistory.size) { index ->
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
                        Card(
                            modifier = Modifier
                                .weight(0.90f)
                                .clickable {
                                    //TODO  search for the text and show the results
                                },
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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
                                Image(
                                    modifier = Modifier.size(SPACING_LARGE.dp),
                                    painter = painterResource(R.drawable.ic_past),
                                    contentDescription = "Photo public icon",
                                )

                                Text(
                                    text = searchHistory.get(index),
                                )
                            }
                        }

                        Icon(
                            modifier = Modifier
                                .size(SPACING_LARGE.dp)
                                .weight(0.10f)
                                .clickable {
                                    onEventSend(MainUiEvent.DeleteFromSearchHistory(index))
                                },
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Photo public icon",
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
private fun SearchHistoryScreenPreview() {
    val viewState = MainViewState(
        isLoading = false,
        error = null,
        subtitle = "Subtitle",
        searchQuery = "",
        searchResultTitle = "Showing results for This",
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
    )

    Content(
        state = viewState,
        onEventSend = {},
        paddingValues = PaddingValues(1.dp),
    )
}
