package com.example.ui.main.bottomtabs.view

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.ui.common.SPACING_MEDIUM
import com.example.ui.common.SPACING_SMALL

@Composable
internal fun ImageOverlayView(
    imageUrl: String,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Box(
        modifier = Modifier.pointerInput(Unit) {
                // Consume all pointer events to block clicks behind the overlay
                awaitPointerEventScope {
                    while (true) {
                        awaitPointerEvent()
                    }
                }
            }
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.9f))
    ) {

        ClearIcon(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(SPACING_MEDIUM.dp)
                .clickable { onClose() },
            tint = Color.White
        )

        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = "Searched photo on overlay",
            modifier = Modifier
                .align(Alignment.Center)
                .padding(SPACING_SMALL.dp)
                .then(if (isLandscape) Modifier.fillMaxHeight() else Modifier.fillMaxWidth()),
            contentScale = if (isLandscape) ContentScale.FillHeight else ContentScale.FillWidth
        )
    }
}
