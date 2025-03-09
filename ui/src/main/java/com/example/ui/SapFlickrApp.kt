package com.example.ui

import androidx.compose.runtime.Composable
import com.example.ui.common.theme.SapFlickrExampleTheme
import com.example.ui.main.MainScreen

@Composable
fun SapFlickrApp() {
    SapFlickrExampleTheme {
        MainScreen()
    }
}