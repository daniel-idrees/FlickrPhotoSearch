package com.example.ui.main.bottomtabs.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun DeleteIcon(modifier: Modifier) {
    Icon(
        modifier = modifier,
        imageVector = Icons.Default.Delete,
        contentDescription = "Delete icon",
    )
}