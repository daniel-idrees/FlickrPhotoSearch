package com.example.ui.main.bottomtabs.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
internal fun ClearIcon(
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current
) {
    Icon(
        imageVector = Icons.Default.Clear,
        contentDescription = "Clear icon",
        modifier = modifier,
        tint = tint
    )
}