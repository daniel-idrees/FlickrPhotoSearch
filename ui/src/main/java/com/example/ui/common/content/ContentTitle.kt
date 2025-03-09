package com.example.ui.common.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ui.common.SPACING_MEDIUM

@Composable
internal fun ContentTitle(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    titleStyle: TextStyle = MaterialTheme.typography.headlineSmall.copy(
        color = MaterialTheme.colorScheme.onSurface
    ),
    subtitleStyle: TextStyle = MaterialTheme.typography.bodyMedium.copy(
        color = MaterialTheme.colorScheme.onSurface
    ),
    subTitleMaxLines: Int = Int.MAX_VALUE,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(SPACING_MEDIUM.dp, Alignment.Top)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            style = titleStyle,
        )
        //Spacer(modifier = Modifier.height(SPACING_MEDIUM.dp))

        subtitle?.let { safeSubtitle ->
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = safeSubtitle,
                style = subtitleStyle,
                maxLines = subTitleMaxLines,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ContentTitlePreview() {
    ContentTitle(
        modifier = Modifier.fillMaxWidth(),
        title = "This is the title",
        subtitle = "This is the sub title"
    )
}

@Preview
@Composable
private fun ContentTitleNoSubtitlePreview() {
    ContentTitle(
        modifier = Modifier.fillMaxWidth(),
        title = "This is the title",
        subtitle = null
    )
}