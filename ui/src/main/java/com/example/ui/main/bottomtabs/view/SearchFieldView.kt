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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.PopupProperties
import com.example.ui.common.SPACING_LARGE
import com.example.ui.common.theme.SapFlickrExampleTheme

@Composable
internal fun SearchFieldView(
    modifier: Modifier = Modifier,
    label: String,
    searchHistory: List<String> = emptyList(),
    searchInputPrefilledText: String = "",
    buttonText: String? = null,
    shouldHaveFocus: Boolean = false,
    searchErrorReceived: Boolean = false,
    doOnSearchTextChange: (String) -> Unit = {},
    doOnSearchRequest: (String) -> Unit,
    doOnSearchHistoryDropDownItemClick: (String) -> Unit = {}
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
                        Icon(
                            modifier = Modifier
                                .size(SPACING_LARGE.dp)
                                .clickable {
                                    textFieldValue = textFieldValue.copy(text = "")
                                    doOnSearchTextChange(textFieldValue.text)
                                },
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear text icon",
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
                                PastIcon(modifier = Modifier.size(SPACING_LARGE.dp))
                            },
                            trailingIcon = {
                                TopLeftArrowIcon(
                                    modifier = Modifier
                                        .size(SPACING_LARGE.dp)
                                )
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

@Preview(showBackground = true)
@PreviewLightDark
@Composable
private fun SearchInputFieldPreview() {
    SapFlickrExampleTheme {
        SearchFieldView(
            label = "Search something",
            doOnSearchRequest = {}
        )
    }
}

@Preview(showBackground = true)
@PreviewLightDark
@Composable
private fun SearchInputFieldWithButtonPreview() {
    SapFlickrExampleTheme {
        SearchFieldView(
            label = "Search something",
            doOnSearchRequest = {},
            buttonText = "Search"
        )
    }
}

@Preview(showBackground = true)
@PreviewLightDark
@Composable
private fun SearchInputFieldWithPrefilledTextPreview() {
    SapFlickrExampleTheme {
        SearchFieldView(
            label = "Search something",
            doOnSearchRequest = {},
            searchInputPrefilledText = "prefilled",
            buttonText = "Search"
        )
    }
}