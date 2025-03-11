package com.example.ui.main.bottomtabs.view

import androidx.annotation.StringRes
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign

@Composable
internal fun ButtonWithTextView(
    modifier: Modifier = Modifier,
    buttonText: String,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
    ) {
        TextBodyMedium(
            textAlign = TextAlign.Center,
            text = buttonText
        )
    }
}