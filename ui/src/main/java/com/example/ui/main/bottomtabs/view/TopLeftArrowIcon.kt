package com.example.ui.main.bottomtabs.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
internal fun TopLeftArrowIcon(modifier: Modifier) {
    Icon(
        imageVector = Icons.AutoMirrored.Default.ArrowBack,
        contentDescription = "Top-left arrow icon",
        modifier = modifier
    )
}