package com.example.ui.main

import androidx.lifecycle.viewModelScope
import com.example.domain.PhotoSearchResult
import com.example.domain.usecase.GetPhotoListUseCase
import com.example.ui.common.content.ContentErrorConfig
import com.example.ui.common.mvi.MviViewModel
import com.example.ui.main.bottomtabs.screen.BottomBarScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(
    private val getPhotoListUseCase: GetPhotoListUseCase
) : MviViewModel<MainUiEvent, MainViewState, MainUiEffect>() {

    override fun setInitialState(): MainViewState = MainViewState(
        title = "Flickr Photo Search"
    )

    override fun handleEvents(event: MainUiEvent) {
        when (event) {
            is MainUiEvent.OnSearchRequest -> {
                if (event.searchQuery.isEmpty() || event.searchQuery.isBlank()) {
                    setEffect {
                        MainUiEffect.EmptyTextError("Enter something to search")
                    }
                } else {
                    search(searchQuery = event.searchQuery, fromScreen = event.fromScreen)
                }
            }

            is MainUiEvent.OnPhotoItemClicked -> {
                // TODO MainUiEffect.Navigation.SwitchScreen(screenRoute = "detail")
            }

            is MainUiEvent.OnBackPressed -> setEffect { MainUiEffect.Navigation.Pop(event.fromScreen) }
            is MainUiEvent.OnSearchTextChange -> setState {
                copy(
                    error = null,
                    searchQuery = event.searchQuery
                )
            }

            MainUiEvent.OnClearAllButtonClick -> setState { copy(searchHistory = ArrayDeque()) }
            is MainUiEvent.DeleteFromSearchHistory -> {
                setState {
                    val newSearchHistory = ArrayDeque(searchHistory).apply {
                        removeAt(event.index)
                    }
                    copy(
                        searchHistory = newSearchHistory
                    )
                }
            }
        }
    }

    private fun search(searchQuery: String, fromScreen: BottomBarScreen) {
        setState {
            copy(
                isLoading = true,
                searchQuery = searchQuery,
                error = null,
                searchHistory = getUpdatedSearchHistory(searchQuery),
            )
        }

        viewModelScope.launch {
            getPhotoListUseCase(searchQuery).collect { result ->
                when (result) {
                    PhotoSearchResult.Empty -> setState {
                        copy(
                            subtitle = "",
                            isLoading = false,
                            error = ContentErrorConfig(
                                errorTitle = "Nothing found...",
                                errorSubTitle = "Unable to find anything. Try something else",
                                onRetry = { search(searchQuery, fromScreen) },
                                retryButtonText = "Try Again"
                            )
                        )
                    }

                    is PhotoSearchResult.Error -> setState {
                        copy(
                            subtitle = "",
                            isLoading = false,
                            error = ContentErrorConfig(
                                errorTitle = "Oops...",
                                errorSubTitle = result.errorMessage,
                                onRetry = { search(searchQuery, fromScreen) },
                                retryButtonText = "Try Again"
                            )
                        )
                    }

                    is PhotoSearchResult.Success -> {
                        setState {
                            copy(
                                error = null,
                                isLoading = false,
                                photoList = result.photos,
                                searchResultTitle = "Showing results for \"$searchQuery\"",
                            )
                        }
                        if (fromScreen != BottomBarScreen.Search) {
                            setEffect {
                                MainUiEffect.Navigation.SwitchScreen(
                                    BottomBarScreen.Search.route
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getUpdatedSearchHistory(searchQuery: String): ArrayDeque<String> {
        val searchHistory = viewState.value.searchHistory
        searchHistory.addFirst(searchQuery)
        return searchHistory
    }
}
