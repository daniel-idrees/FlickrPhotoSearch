@file:OptIn(ExperimentalFoundationApi::class)

package com.example.ui.main.bottomtabs.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.domain.model.PhotoItem
import com.example.ui.R
import com.example.ui.common.SPACING_EXTRA_LARGE
import com.example.ui.common.SPACING_LARGE
import com.example.ui.common.SPACING_MEDIUM
import com.example.ui.common.SPACING_SMALL
import com.example.ui.common.content.ContentError
import com.example.ui.common.content.ContentScreen
import com.example.ui.common.content.ContentTitle
import com.example.ui.common.keyboardAsState
import com.example.ui.common.theme.SapFlickrExampleTheme
import com.example.ui.main.MainUiEvent
import com.example.ui.main.MainViewModel
import com.example.ui.main.MainViewState
import com.example.ui.main.bottomtabs.screen.config.BottomBarScreen
import com.example.ui.main.bottomtabs.view.PhotoListItemView
import com.example.ui.main.bottomtabs.view.SearchFieldView
import com.example.ui.main.bottomtabs.view.TopArrowIcon
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val showFab by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex > 5
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
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
            val photosResultAvailable = state.photoList.isNotEmpty()
            if (!photosResultAvailable) {
                ContentTitle(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(R.string.search_screen_title),
                )
            }
            LazyColumn(
                modifier = Modifier.weight(1f),
                state = lazyListState
            ) {
                stickyHeader {
                    val prefilledText =
                        if (photosResultAvailable) state.searchHistory.first() else state.searchQuery
                    val shouldShowButton = !photosResultAvailable

                    SearchFieldView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(if (!photosResultAvailable) Modifier.padding(top = SPACING_LARGE.dp) else Modifier),
                        searchInputPrefilledText = prefilledText,
                        searchHistory = state.searchHistory,
                        label = stringResource(R.string.search_screen_search_field_label),
                        shouldHaveFocus = !photosResultAvailable,
                        buttonText = if (shouldShowButton && state.error == null) stringResource(R.string.search_screen_search_button_text) else null,
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
                        doOnSearchTextChange = { text ->
                            onEventSend(
                                MainUiEvent.OnSearchTextChange(
                                    text
                                )
                            )
                        }
                    )
                }

                if (photosResultAvailable) {
                    item {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = SPACING_MEDIUM.dp),
                            text = stringResource(
                                state.searchResultTitleRes,
                                state.searchHistory.first()
                            ),
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    items(state.photoList) { photo ->
                        PhotoListItemView(
                            modifier = Modifier
                                .fillMaxWidth(),
                            photoItem = photo,
                            onPhotoClick = { onEventSend(MainUiEvent.OnPhotoItemClicked(photo)) }
                        )
                        Spacer(modifier = Modifier.height(SPACING_LARGE.dp))
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = SPACING_EXTRA_LARGE.dp)
                        )
                        Spacer(modifier = Modifier.height(SPACING_SMALL.dp))
                    }
                } else if (state.error != null) {
                    item {
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
                    }
                } else {
                    item {
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
        }
        if (showFab) {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        lazyListState.scrollToItem(0)
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(SPACING_MEDIUM.dp)
            ) {
                TopArrowIcon(modifier = Modifier.size(SPACING_LARGE.dp))
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun SearchPreview(
    @PreviewParameter(SearchPreviewParameterProvider::class) viewState: MainViewState
) {
    SapFlickrExampleTheme {
        ContentScreen {
            Content(
                paddingValues = PaddingValues(1.dp),
                state = viewState,
                onEventSend = {}
            )
        }
    }
}

private class SearchPreviewParameterProvider : PreviewParameterProvider<MainViewState> {
    override val values = sequenceOf(
        MainViewState(),
        MainViewState(searchQuery = "query"),
        MainViewState(isLoading = true),
        MainViewState(
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
                    title = "Photo One",
                    url = "url",
                    isPublic = true,
                    isFriend = true,
                    isFamily = true
                )
            ),
            searchHistory = ArrayDeque(listOf("query", "query2"))
        ), MainViewState(
            searchQuery = "query",
            photoList = listOf(
                PhotoItem(
                    title = "Photo Two",
                    url = "url",
                    isPublic = false,
                    isFriend = false,
                    isFamily = false
                )
            ),
            searchHistory = ArrayDeque(listOf("query", "query2"))
        ), MainViewState(
            searchQuery = "query",
            photoList = listOf(
                PhotoItem(
                    title = "Photo Three",
                    url = "url",
                    isPublic = false,
                    isFriend = true,
                    isFamily = false
                )
            ),
            searchHistory = ArrayDeque(listOf("query", "query2"))
        )
    )
}