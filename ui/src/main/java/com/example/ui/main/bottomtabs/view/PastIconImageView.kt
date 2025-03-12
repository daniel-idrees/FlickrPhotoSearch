package com.example.ui.main.bottomtabs.view

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.ui.R

@Composable
internal fun PastIconImageView(modifier: Modifier) {
    Image(
        modifier = modifier,
        painter = painterResource(R.drawable.ic_past),
        contentDescription = "Past icon",
    )
}