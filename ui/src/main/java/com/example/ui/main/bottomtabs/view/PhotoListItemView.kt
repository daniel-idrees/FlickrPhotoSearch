package com.example.ui.main.bottomtabs.view

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil3.EventListener
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.crossfade
import com.example.domain.model.SearchedPhoto
import com.example.ui.R
import com.example.ui.common.SPACING_EXTRA_LARGE
import com.example.ui.common.SPACING_LARGE
import com.example.ui.common.SPACING_MEDIUM
import com.example.ui.common.SPACING_SMALL
import com.example.ui.common.theme.FlickrPhotoSearchTheme

@Composable
internal fun PhotoListItemView(
    modifier: Modifier = Modifier,
    photo: SearchedPhoto,
    onPhotoClick: (Boolean) -> Unit
) {
    val context = LocalContext.current
    var hasImageLoadedSuccessfully by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier.padding(bottom = SPACING_LARGE.dp),
        verticalArrangement = Arrangement.spacedBy(SPACING_SMALL.dp)
    ) {

        val configuration = LocalConfiguration.current
        val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        val screenWidth = configuration.screenWidthDp.dp

        Card(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .background(Color.Transparent)
                .then(if (isLandscape) Modifier.widthIn(max = screenWidth * 0.4f) else Modifier)
                .clickable { onPhotoClick(hasImageLoadedSuccessfully) },
            shape = RoundedCornerShape(SPACING_MEDIUM.dp),
        ) {

            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(photo.url)
                    .crossfade(true)
                    .listener(object : EventListener() {
                        override fun onSuccess(request: ImageRequest, result: SuccessResult) {
                            super.onSuccess(request, result)
                            hasImageLoadedSuccessfully = true
                        }
                    })
                    .build(),
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
                contentDescription = "Searched photo",
                placeholder = painterResource(id = R.drawable.ic_placeholder),
                error = painterResource(id = R.drawable.ic_placeholder)
            )
        }

        TextBodyMedium(
            text = photo.title,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val iconModifier = Modifier.size(SPACING_LARGE.dp)
            if (photo.isPublic) {
                PublicIconImageView(modifier = iconModifier)
            } else {
                PrivateIconImageView(modifier = iconModifier)
            }
            if (photo.isFriend) {
                FriendsIconImageView(modifier = iconModifier)
            }
            if (photo.isFamily) {
                FamilyIconImageView(modifier = iconModifier)
            }
        }

        Spacer(modifier = Modifier.height(SPACING_SMALL.dp))
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = SPACING_EXTRA_LARGE.dp)
        )
    }
}

@PreviewLightDark
@Composable
private fun PhotoListItemWithAllIconsPreview() {
    PhotoListItemView(
        photo = SearchedPhoto(
            title = "Photo title",
            url = " https://farm66.staticflickr.com/65535/54375913088_62172768d8.jpg",
            isPublic = true,
            isFriend = true,
            isFamily = true
        ),
        onPhotoClick = { }
    )
}

@PreviewLightDark
@Composable
private fun PhotoListItemWithFriendPreview() {
    FlickrPhotoSearchTheme {
        PhotoListItemView(
            photo = SearchedPhoto(
                title = "Photo title",
                url = " https://farm66.staticflickr.com/65535/54375913088_62172768d8.jpg",
                isPublic = false,
                isFriend = true,
                isFamily = false
            ),
            onPhotoClick = { }
        )
    }
}