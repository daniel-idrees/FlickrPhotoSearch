@file:OptIn(ExperimentalFoundationApi::class)

package com.example.ui.main.bottomtabs.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.model.Photo
import com.example.ui.R
import com.example.ui.common.SPACING_LARGE
import com.example.ui.common.SPACING_MEDIUM
import com.example.ui.common.content.ContentError
import com.example.ui.common.content.ContentErrorConfig
import com.example.ui.common.content.ContentScreen
import com.example.ui.common.content.ContentTitle
import com.example.ui.common.keyboardAsState
import com.example.ui.common.theme.FlickrPhotoSearchTheme
import com.example.ui.main.MainUiEvent
import com.example.ui.main.MainViewModel
import com.example.ui.main.MainViewState
import com.example.ui.main.bottomtabs.SearchUiEffect
import com.example.ui.main.bottomtabs.SearchUiEvent
import com.example.ui.main.bottomtabs.SearchViewModel
import com.example.ui.main.bottomtabs.SearchViewState
import com.example.ui.main.bottomtabs.screen.config.BottomBarScreen
import com.example.ui.main.bottomtabs.view.PhotoListItemView
import com.example.ui.main.bottomtabs.view.SearchFieldView
import com.example.ui.main.bottomtabs.view.TextBodyMedium
import com.example.ui.main.bottomtabs.view.TopArrowIcon
import com.example.ui.main.bottomtabs.view.ImageOverlayView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun SearchScreen(
    mainViewModel: MainViewModel,
    searchViewModel: SearchViewModel = hiltViewModel(),
) {
    val mainViewState by mainViewModel.viewState.collectAsStateWithLifecycle()
    val searchViewState by searchViewModel.viewState.collectAsStateWithLifecycle()
    var isPhotoOverlayVisible by rememberSaveable { mutableStateOf(false) }

    ContentScreen(
        isLoading = mainViewState.isLoading,
        backPressHandler = {
            if (isPhotoOverlayVisible) {
                searchViewModel.setEvent(SearchUiEvent.ClearPhotoOverlay)
            } else {
                mainViewModel.setEvent(MainUiEvent.OnNavigateBackRequest(fromScreen = BottomBarScreen.Search))
            }
        }
    ) { paddingValues ->
        Content(
            photoList = mainViewState.photoList,
            lastSearch = mainViewState.lastSearch,
            searchQuery = mainViewState.searchQuery,
            errorConfig = mainViewState.error,
            searchHistory = mainViewState.searchHistory,
            searchResultTitleRes = mainViewState.searchResultTitleRes,
            searchViewState = searchViewState,
            onMainUiEventSend = { mainViewModel.setEvent(it) },
            onSearchUiEventSend = { searchViewModel.setEvent(it) },
            shouldPhotoOverlayBeVisible = isPhotoOverlayVisible,
            paddingValues = paddingValues
        )
    }

    LaunchedEffect(Unit) {
        searchViewModel.effect.collect { effect ->
            isPhotoOverlayVisible = when (effect) {
                SearchUiEffect.HidePhotoOverlay -> false
                SearchUiEffect.ShowPhotoOverlay -> true
            }
        }
    }
}

@Composable
private fun Content(
    searchViewState: SearchViewState,
    onMainUiEventSend: (MainUiEvent) -> Unit,
    onSearchUiEventSend: (SearchUiEvent) -> Unit,
    shouldPhotoOverlayBeVisible: Boolean,
    paddingValues: PaddingValues,
    photoList: List<Photo>,
    lastSearch: String,
    searchQuery: String,
    errorConfig: ContentErrorConfig?,
    searchResultTitleRes: Int,
    searchHistory: List<String>,
) {
    val hasError = errorConfig != null
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val showFab by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex > 5 //show floating button after 5 items
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
            val photosResultAvailable = photoList.isNotEmpty()
            if (!photosResultAvailable) {
                ContentTitle(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(R.string.search_screen_title),
                )
            }
            LazyColumn(
                modifier = Modifier
                    .weight(1f),
                state = lazyListState
            ) {
                stickyHeader {
                    val prefilledText =
                        if (photosResultAvailable) lastSearch else searchQuery

                    val shouldShowButton = !photosResultAvailable

                    SearchFieldView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(if (!photosResultAvailable) Modifier.padding(top = SPACING_LARGE.dp) else Modifier),
                        searchInputPrefilledText = prefilledText,
                        searchHistory = searchHistory,
                        label = stringResource(R.string.search_screen_search_field_label),
                        shouldHaveFocus = !photosResultAvailable,
                        buttonText = if (shouldShowButton && !hasError) stringResource(
                            R.string.search_screen_search_button_text
                        ) else null,
                        searchErrorReceived = hasError,
                        doOnSearchRequest = { text ->
                            onMainUiEventSend(
                                MainUiEvent.RequestSearch(
                                    searchQuery = text,
                                    BottomBarScreen.Search
                                )
                            )
                        },
                        doOnSearchHistoryDropDownItemClick = { text ->
                            onMainUiEventSend(
                                MainUiEvent.OnSearchHistoryItemSelected(
                                    searchQuery = text,
                                    BottomBarScreen.Home
                                )
                            )
                        },
                        doOnSearchTextChange = { text ->
                            onMainUiEventSend(
                                MainUiEvent.OnSearchQueryChange(
                                    text
                                )
                            )
                        },
                        doOnClearHistoryClick = { index ->
                            onMainUiEventSend(MainUiEvent.RemoveSearchHistory(index))
                        }
                    )
                }

                if (photosResultAvailable) {
                    item {
                        TextBodyMedium(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = SPACING_MEDIUM.dp),
                            text = stringResource(
                                searchResultTitleRes,
                                lastSearch
                            ),
                        )
                    }
                    items(photoList) { photo ->
                        PhotoListItemView(
                            modifier = Modifier
                                .fillMaxWidth(),
                            photo = photo,
                            onPhotoClick = { hasPhotoLoadedSuccessfully ->
                                if (hasPhotoLoadedSuccessfully) {
                                    onSearchUiEventSend(SearchUiEvent.OnPhotoClick(photo))
                                }
                            }
                        )
                    }
                } else if (errorConfig != null) {
                    item {
                        ContentError(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(
                                    paddingValues = PaddingValues(
                                        top = SPACING_MEDIUM.dp,
                                        bottom = paddingValues.calculateBottomPadding()
                                    )
                                ),
                            contentErrorConfig = errorConfig
                        )
                    }
                } else {
                    item {
                        val isKeyboardOpen by keyboardAsState()
                        LaunchedEffect(isKeyboardOpen) {
                            delay(300)
                            if (!isKeyboardOpen) {
                                onMainUiEventSend(
                                    MainUiEvent.OnNavigateBackRequest(
                                        BottomBarScreen.Search
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(SPACING_MEDIUM.dp), visible = showFab
        ) {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        lazyListState.scrollToItem(0)
                    }
                },
                modifier = Modifier

            ) {
                TopArrowIcon(modifier = Modifier.size(SPACING_LARGE.dp))
            }
        }
    }


    AnimatedVisibility(
        visible = shouldPhotoOverlayBeVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        searchViewState.selectedPhoto?.let { image ->
            ImageOverlayView(
                imageUrl = image.url,
                onClose = {
                    onSearchUiEventSend(SearchUiEvent.ClearPhotoOverlay)
                }
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun SearchPreview(
    @PreviewParameter(SearchPreviewParameterProvider::class) viewState: MainViewState,
) {
    FlickrPhotoSearchTheme {
        Content(
            photoList = viewState.photoList,
            lastSearch = viewState.lastSearch,
            searchQuery = viewState.searchQuery,
            errorConfig = viewState.error,
            searchHistory = viewState.searchHistory,
            searchResultTitleRes = viewState.searchResultTitleRes,
            searchViewState = SearchViewState(selectedPhoto = null),
            onSearchUiEventSend = {},
            onMainUiEventSend = {},
            shouldPhotoOverlayBeVisible = false,
            paddingValues = PaddingValues(1.dp)
        )
    }
}

private class SearchPreviewParameterProvider : PreviewParameterProvider<MainViewState> {
    val viewState = MainViewState(
        photoList = emptyList(),
        searchHistory = ArrayDeque(),
        isLoading = false,
        error = null,
        searchQuery = "",
        lastSearch = "",
        searchResultTitleRes = 0
    )

    override val values = sequenceOf(
        viewState,
        viewState.copy(searchQuery = "query"),
        viewState.copy(isLoading = true),
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
                    title = "Photo One",
                    url = "url",
                    isPublic = true,
                    isFriend = true,
                    isFamily = true
                )
            ),
            searchHistory = ArrayDeque(listOf("query", "query2")),
        ), viewState.copy(
            searchQuery = "query",
            photoList = listOf(
                Photo(
                    title = "Photo Two",
                    url = "url",
                    isPublic = false,
                    isFriend = false,
                    isFamily = false
                )
            ),
            searchHistory = ArrayDeque(listOf("query", "query2")),
        ), viewState.copy(
            searchQuery = "query",
            photoList = listOf(
                Photo(
                    title = "Photo Three",
                    url = "url",
                    isPublic = false,
                    isFriend = true,
                    isFamily = false
                )
            ),
            searchHistory = ArrayDeque(listOf("query", "query2"))
        ),
        viewState.copy(
            searchQuery = "query",
            photoList = emptyList(),
            searchHistory = ArrayDeque(listOf("query", "query2")),
            error = ContentErrorConfig(
                errorTitleRes = ContentErrorConfig.ErrorMessage.Text("Something went wrong"),
                errorSubTitleRes = ContentErrorConfig.ErrorMessage.Text("Please check something and try again."),
            ) {}
        )
    )
}