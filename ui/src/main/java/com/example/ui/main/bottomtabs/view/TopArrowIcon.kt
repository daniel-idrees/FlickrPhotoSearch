package com.example.ui.main.bottomtabs.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun TopArrowIcon(modifier: Modifier = Modifier) {
    Icon(
        modifier = modifier.graphicsLayer {
            rotationZ = 90f
        },
        imageVector = Icons.AutoMirrored.Default.ArrowBack,
        contentDescription = "Scroll to Top Icon"
    )
}