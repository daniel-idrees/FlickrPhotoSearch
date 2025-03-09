package com.example.ui.main.bottomtabs.view

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.domain.model.PhotoItem
import com.example.ui.R
import com.example.ui.common.SPACING_LARGE
import com.example.ui.common.SPACING_SMALL

@Composable
internal fun PhotoListItemView(photoItem: PhotoItem, onItemClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(SPACING_SMALL.dp)
            .clickable {
                onItemClick()
            },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            AsyncImage(
                model = photoItem.url,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Text(
                text = photoItem.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                if (photoItem.isPublic) {
                    Image(
                        modifier = Modifier.size(SPACING_LARGE.dp),
                        painter = painterResource(id = R.drawable.ic_public),
                        contentDescription = "Photo public icon",
                    )
                }

                if (photoItem.isFriend) {
                    Image(
                        modifier = Modifier.size(SPACING_LARGE.dp),
                        painter = painterResource(id = R.drawable.ic_friends),
                        contentDescription = "Photo friends icon"
                    )
                }

                if (photoItem.isFamily) {
                    Image(
                        modifier = Modifier.size(SPACING_LARGE.dp),
                        painter = painterResource(id = R.drawable.ic_family),
                        contentDescription = "Photo family icon"
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PhotoListItemPreview() {
    PhotoListItemView(
        photoItem = PhotoItem(
            title = "Photo title",
            url = " https://farm66.staticflickr.com/65535/54375913088_62172768d8.jpg",
            isPublic = true,
            isFriend = true,
            isFamily = true
        )
    ) {}
}