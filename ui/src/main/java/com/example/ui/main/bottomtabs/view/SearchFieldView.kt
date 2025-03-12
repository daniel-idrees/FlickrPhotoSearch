package com.example.ui.main.bottomtabs.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.PopupProperties
import com.example.ui.common.SPACING_LARGE
import com.example.ui.common.theme.FlickrPhotoSearchTheme

@Composable
internal fun SearchFieldView(
    modifier: Modifier = Modifier,
    label: String,
    searchHistory: List<String> = emptyList(),
    searchInputPrefilledText: String = "",
    buttonText: String? = null,
    shouldHaveFocus: Boolean = false,
    searchErrorReceived: Boolean = false,
    doOnSearchTextChange: (updatedQuery: String) -> Unit = {},
    doOnSearchRequest: (query: String) -> Unit,
    doOnSearchHistoryDropDownItemClick: (query: String) -> Unit = {},
    doOnClearHistoryClick: ((index: Int) -> Unit)? = null
) {
    val shouldShowButton = !buttonText.isNullOrEmpty()

    val focusManager = LocalFocusManager.current
    val density = LocalDensity.current
    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = searchInputPrefilledText,
                selection = TextRange(searchInputPrefilledText.length)
            )
        )
    }
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = SPACING_LARGE.dp,
            alignment = Alignment.CenterVertically,
        ),
    ) {
        val textFieldModifier = Modifier
            .fillMaxWidth()
            .then(if (shouldHaveFocus) Modifier.focusRequester(focusRequester) else Modifier)
            .onFocusChanged { focusState -> isFocused = focusState.isFocused }
            .onGloballyPositioned { coordinates ->
                textFieldSize = coordinates.size.toSize()
            }
        Box {
            OutlinedTextField(
                modifier = textFieldModifier,
                value = textFieldValue,
                onValueChange = { newTextFieldValue ->
                    textFieldValue = newTextFieldValue
                    doOnSearchTextChange(newTextFieldValue.text)
                },
                label = { Text(label) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        doOnSearchRequest(textFieldValue.text)
                        focusManager.clearFocus()
                    }
                ),
                trailingIcon = {
                    if (textFieldValue.text.isNotEmpty()) {
                        ClearIcon(
                            modifier = Modifier
                                .size(SPACING_LARGE.dp)
                                .clickable {
                                    textFieldValue = textFieldValue.copy(text = "")
                                    doOnSearchTextChange(textFieldValue.text)
                                }
                        )
                    }
                }
            )

            if (isFocused && searchHistory.isNotEmpty() && !searchErrorReceived) {
                DropdownMenu(
                    expanded = isFocused,
                    onDismissRequest = { isFocused = false },
                    modifier = Modifier
                        .width(with(density) { textFieldSize.width.toDp() }),
                    properties = PopupProperties(focusable = false)
                ) {
                    searchHistory.forEachIndexed { index, suggestion ->
                        if (index > 9) {
                            return@forEachIndexed
                        }

                        DropdownMenuItem(
                            leadingIcon = {
                                PastIconImageView(modifier = Modifier.size(SPACING_LARGE.dp))
                            },
                            trailingIcon = {
                                if (doOnClearHistoryClick != null) {
                                    ClearIcon(
                                        modifier = Modifier
                                            .size(SPACING_LARGE.dp)
                                            .clickable {
                                                doOnClearHistoryClick(index)
                                            }
                                    )
                                } else {
                                    TopLeftArrowIcon(
                                        modifier = Modifier
                                            .size(SPACING_LARGE.dp)
                                    )
                                }
                            },
                            text = { Text(text = suggestion) },
                            onClick = {
                                doOnSearchHistoryDropDownItemClick(suggestion)
                                focusManager.clearFocus()
                                isFocused = false
                            }
                        )
                    }
                }
            }
        }

        if (shouldShowButton && buttonText != null) {
            ButtonWithTextView(
                modifier = Modifier
                    .width(with(density) { textFieldSize.width.toDp() }),
                buttonText = buttonText,
                onClick = {
                    doOnSearchRequest(textFieldValue.text)
                    focusManager.clearFocus()
                }
            )
        }
    }

    LaunchedEffect(shouldHaveFocus) {
        if (shouldHaveFocus) {
            focusRequester.requestFocus()
            textFieldValue = textFieldValue.copy(
                selection = TextRange(textFieldValue.text.length)
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun SearchInputFieldPreview() {
    FlickrPhotoSearchTheme {
        SearchFieldView(
            label = "Search something",
            doOnSearchRequest = {}
        )
    }
}

@PreviewLightDark
@Composable
private fun SearchInputFieldWithButtonPreview() {
    FlickrPhotoSearchTheme {
        SearchFieldView(
            label = "Search something",
            doOnSearchRequest = {},
            buttonText = "Search"
        )
    }
}

@PreviewLightDark
@Composable
private fun SearchInputFieldWithPrefilledTextPreview() {
    FlickrPhotoSearchTheme {
        SearchFieldView(
            label = "Search something",
            doOnSearchRequest = {},
            searchInputPrefilledText = "prefilled",
            buttonText = "Search"
        )
    }
}