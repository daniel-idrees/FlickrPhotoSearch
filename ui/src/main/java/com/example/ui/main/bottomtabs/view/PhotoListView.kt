package com.example.ui.main.bottomtabs.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.domain.model.PhotoItem
import com.example.ui.main.MainUiEvent
import com.example.ui.common.SPACING_MEDIUM

@Composable
internal fun ListView(
    items: List<PhotoItem>,
    paddingValues: PaddingValues,
    onItemClick: (MainUiEvent) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(SPACING_MEDIUM.dp),
        contentPadding = PaddingValues(
            top = SPACING_MEDIUM.dp,
            bottom = paddingValues.calculateBottomPadding()
        ),
    ) {
        items(items) { photo ->
            PhotoListItemView(photo) { onItemClick(MainUiEvent.OnPhotoItemClicked(photo)) }
        }
    }
}