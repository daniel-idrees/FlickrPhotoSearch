package com.example.ui

import androidx.compose.runtime.Composable
import com.example.ui.common.theme.FlickrPhotoSearchTheme
import com.example.ui.main.MainScreen

@Composable
fun FlickrPhotoSearchApp() {
    FlickrPhotoSearchTheme {
        MainScreen()
    }
}