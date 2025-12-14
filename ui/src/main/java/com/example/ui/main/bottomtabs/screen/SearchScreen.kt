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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.domain.model.SearchedPhoto
import com.example.domain.model.Visibility
import com.example.ui.R
import com.example.ui.common.SPACING_LARGE
import com.example.ui.common.SPACING_MEDIUM
import com.example.ui.common.content.ContentError
import com.example.ui.common.content.ContentErrorConfig
import com.example.ui.common.content.ContentScreen
import com.example.ui.common.content.ContentTitle
import com.example.ui.common.keyboardAsState
import com.example.ui.common.theme.FlickrPhotoSearchTheme
import com.example.ui.main.MainUiAction
import com.example.ui.main.MainViewModel
import com.example.ui.main.MainViewState
import com.example.ui.main.bottomtabs.SearchUiEffect
import com.example.ui.main.bottomtabs.SearchUiAction
import com.example.ui.main.bottomtabs.SearchViewModel
import com.example.ui.main.bottomtabs.SearchViewState
import com.example.ui.main.bottomtabs.screen.config.BottomBarScreen
import com.example.ui.main.bottomtabs.view.ImageOverlayView
import com.example.ui.main.bottomtabs.view.PhotoListItemView
import com.example.ui.main.bottomtabs.view.SearchFieldView
import com.example.ui.main.bottomtabs.view.TextBodyMedium
import com.example.ui.main.bottomtabs.view.TopArrowIcon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
internal fun SearchScreen(
    mainViewModel: MainViewModel,
    searchViewModel: SearchViewModel = hiltViewModel(),
) {
    val mainViewState by mainViewModel.viewState.collectAsStateWithLifecycle()
    val searchViewState by searchViewModel.viewState.collectAsStateWithLifecycle()
    var isPhotoOverlayVisible by rememberSaveable { mutableStateOf(false) }

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                searchViewModel.effect.collect { effect ->
                    isPhotoOverlayVisible = when (effect) {
                        SearchUiEffect.HidePhotoOverlay -> false
                        SearchUiEffect.ShowPhotoOverlay -> true
                    }
                }
            }
        }
    }

    ContentScreen(
        isLoading = mainViewState.isLoading,
        backPressHandler = {
            if (isPhotoOverlayVisible) {
                searchViewModel.setAction(SearchUiAction.ClearPhotoOverlay)
            } else {
                mainViewModel.setAction(MainUiAction.OnNavigateBackRequest(fromScreen = BottomBarScreen.Search))
            }
        }
    ) { paddingValues ->
        Content(
            searchedPhotoList = mainViewState.searchedPhotos,
            lastSearch = mainViewState.lastSearch,
            searchQuery = mainViewState.searchQuery,
            errorConfig = mainViewState.error,
            searchHistory = mainViewState.searchHistory,
            searchViewState = searchViewState,
            onMainUiEventSend = { mainViewModel.setAction(it) },
            onSearchUiEventSend = { searchViewModel.setAction(it) },
            shouldPhotoOverlayBeVisible = isPhotoOverlayVisible,
            paddingValues = paddingValues
        )
    }
}

@Composable
private fun Content(
    searchViewState: SearchViewState,
    onMainUiEventSend: (MainUiAction) -> Unit,
    onSearchUiEventSend: (SearchUiAction) -> Unit,
    shouldPhotoOverlayBeVisible: Boolean,
    paddingValues: PaddingValues,
    searchedPhotoList: List<SearchedPhoto>,
    lastSearch: String,
    searchQuery: String,
    errorConfig: ContentErrorConfig?,
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
            val photosResultAvailable = searchedPhotoList.isNotEmpty()
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
                                MainUiAction.RequestSearch(
                                    searchQuery = text,
                                    BottomBarScreen.Search
                                )
                            )
                        },
                        doOnSearchHistoryDropDownItemClick = { text ->
                            onMainUiEventSend(
                                MainUiAction.OnSearchHistoryItemSelected(
                                    searchQuery = text,
                                    BottomBarScreen.Home
                                )
                            )
                        },
                        doOnSearchTextChange = { text ->
                            onMainUiEventSend(
                                MainUiAction.OnSearchQueryChange(
                                    text
                                )
                            )
                        },
                        doOnClearHistoryClick = { index ->
                            onMainUiEventSend(MainUiAction.RemoveSearchHistory(index))
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
                                R.string.main_view_model_success_result_title,
                                lastSearch
                            ),
                        )
                    }
                    items(searchedPhotoList) { photo ->
                        PhotoListItemView(
                            modifier = Modifier
                                .fillMaxWidth(),
                            photo = photo,
                            onPhotoClick = { hasPhotoLoadedSuccessfully ->
                                if (hasPhotoLoadedSuccessfully) {
                                    onSearchUiEventSend(SearchUiAction.OnPhotoClick(photo))
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
                                    MainUiAction.OnNavigateBackRequest(
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
                    onSearchUiEventSend(SearchUiAction.ClearPhotoOverlay)
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
            searchedPhotoList = viewState.searchedPhotos,
            lastSearch = viewState.lastSearch,
            searchQuery = viewState.searchQuery,
            errorConfig = viewState.error,
            searchHistory = viewState.searchHistory,
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
        searchedPhotos = emptyList(),
        searchHistory = emptyList(),
        isLoading = false,
        error = null,
        searchQuery = "",
        lastSearch = "",
    )

    override val values = sequenceOf(
        viewState,
        viewState.copy(searchQuery = "query"),
        viewState.copy(isLoading = true),
        viewState.copy(
            searchQuery = "query",
            searchedPhotos = listOf(
                SearchedPhoto(
                    title = "Photo Two",
                    url = "url",
                    visibility = Visibility.PRIVATE
                )
            ),
            searchHistory = listOf("query", "query2"),
            lastSearch = "query"
        ), viewState.copy(
            searchQuery = "query",
            searchedPhotos = listOf(
                SearchedPhoto(
                    title = "Photo Three",
                    url = "url",
                    visibility = Visibility.FRIEND
                )
            ),
            searchHistory = listOf("query", "query2"),
            lastSearch = "query"
        ),
        viewState.copy(
            searchQuery = "query",
            searchedPhotos = listOf(
                SearchedPhoto(
                    title = "Photo Three",
                    url = "url",
                    visibility = Visibility.FAMILY
                )
            ),
            searchHistory = listOf("query", "query2"),
            lastSearch = "query"
        ),
        viewState.copy(
            searchQuery = "query",
            searchedPhotos = emptyList(),
            searchHistory = listOf("query", "query2"),
            error = ContentErrorConfig(
                errorTitleRes = ContentErrorConfig.ErrorMessage.Text("Something went wrong"),
                errorSubTitleRes = ContentErrorConfig.ErrorMessage.Text("Please check something and try again."),
            ) {}
        )
    )
}