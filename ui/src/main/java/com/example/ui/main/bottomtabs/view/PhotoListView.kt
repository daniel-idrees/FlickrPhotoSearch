package com.example.ui.main.bottomtabs.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.domain.model.PhotoItem
import com.example.ui.common.SPACING_EXTRA_LARGE
import com.example.ui.common.SPACING_LARGE
import com.example.ui.common.SPACING_MEDIUM
import com.example.ui.common.SPACING_SMALL
import com.example.ui.common.theme.SapFlickrExampleTheme

@Composable
internal fun ListView(
    modifier: Modifier = Modifier,
    items: List<PhotoItem>,
    paddingValues: PaddingValues,
    onItemClick: (PhotoItem) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(SPACING_LARGE.dp),
        contentPadding = PaddingValues(
            top = SPACING_MEDIUM.dp,
            bottom = paddingValues.calculateBottomPadding()
        ),
    ) {
        items(items) { photo ->
            PhotoListItemView(
                modifier = Modifier
                    .fillMaxWidth(),
                photoItem = photo,
                onPhotoClick = { onItemClick(photo) }
            )
            Spacer(modifier = Modifier.height(SPACING_LARGE.dp))
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = SPACING_EXTRA_LARGE.dp)
            )
            Spacer(modifier = Modifier.height(SPACING_SMALL.dp))
        }
    }
}

@Preview(showBackground = true)
@PreviewLightDark
@Composable
private fun PhotoListViewPreview() {
    SapFlickrExampleTheme {
        ListView(
            items = listOf(
                PhotoItem(
                    title = "Photo title",
                    url = " https://farm66.staticflickr.com/65535/54375913088_62172768d8.jpg",
                    isPublic = false,
                    isFriend = true,
                    isFamily = false
                ),
                PhotoItem(
                    title = "Photo title",
                    url = " https://farm66.staticflickr.com/65535/54375913088_62172768d8.jpg",
                    isPublic = true,
                    isFriend = true,
                    isFamily = true
                )
            ),
            paddingValues = PaddingValues(1.dp),
        ) {}
    }
}