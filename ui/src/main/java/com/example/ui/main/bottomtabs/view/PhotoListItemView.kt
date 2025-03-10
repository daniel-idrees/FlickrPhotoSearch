package com.example.ui.main.bottomtabs.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.domain.model.PhotoItem
import com.example.ui.R
import com.example.ui.common.SPACING_MEDIUM
import com.example.ui.common.SPACING_SMALL

@Composable
internal fun PhotoListItemView(
    modifier: Modifier = Modifier,
    photoItem: PhotoItem,
    onPhotoClick: () -> Unit
) {
    Column(modifier = modifier) {
        Card(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .background(Color.Transparent)
                .clickable {
                    onPhotoClick()
                },
            shape = RoundedCornerShape(SPACING_MEDIUM.dp),
        ) {
            val context = LocalContext.current

            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(photoItem.url)
                    .crossfade(true)
                    .build(),
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
                contentDescription = "Searched photo",
                placeholder = painterResource(id = R.drawable.ic_placeholder),
                error = painterResource(id = R.drawable.ic_placeholder)
            )
        }
        Text(
            text = photoItem.title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = SPACING_SMALL.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = SPACING_SMALL.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PublicIconImage(photoItem.isPublic)
            FriendsIconImage(photoItem.isFriend)
            FamilyIconImage(photoItem.isFamily)
        }
    }
}

@Preview(showBackground = true)
@PreviewLightDark
@Composable
private fun PhotoListItemWithAllIconsPreview() {
    PhotoListItemView(
        photoItem = PhotoItem(
            title = "Photo title",
            url = " https://farm66.staticflickr.com/65535/54375913088_62172768d8.jpg",
            isPublic = true,
            isFriend = true,
            isFamily = true
        ),
        onPhotoClick = { }
    )
}

@Preview(showBackground = true)
@PreviewLightDark
@Composable
private fun PhotoListItemWithFriendPreview() {
    PhotoListItemView(
        photoItem = PhotoItem(
            title = "Photo title",
            url = " https://farm66.staticflickr.com/65535/54375913088_62172768d8.jpg",
            isPublic = false,
            isFriend = true,
            isFamily = false
        ),
        onPhotoClick = { }
    )
}