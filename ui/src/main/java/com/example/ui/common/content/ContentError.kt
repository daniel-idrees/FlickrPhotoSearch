package com.example.ui.common.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.common.SPACING_EXTRA_LARGE
import com.example.ui.common.SPACING_MEDIUM

@Composable
internal fun ContentError(
    contentErrorConfig: ContentErrorConfig,
    modifier: Modifier = Modifier,
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
        verticalArrangement = Arrangement.spacedBy(SPACING_MEDIUM.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = contentErrorConfig.errorTitle,
            style = titleStyle,
        )

        contentErrorConfig.errorSubTitle.let { safeSubtitle ->
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = safeSubtitle,
                style = subtitleStyle,
                maxLines = subTitleMaxLines,
                overflow = TextOverflow.Ellipsis
            )
        }
        if (contentErrorConfig.onRetry != null && contentErrorConfig.retryButtonText != null) {
            Button(
                modifier = Modifier
                    .padding(horizontal = SPACING_EXTRA_LARGE.dp)
                    .fillMaxWidth(),
                onClick = {
                    contentErrorConfig.onRetry.invoke()
                },
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    text = contentErrorConfig.retryButtonText,
                    fontSize = 15.sp,
                )
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
private fun ContentErrorPreview() {
    ContentError(
        ContentErrorConfig(
            errorTitle = "Something went wrong",
            errorSubTitle = "Please check something",
            onRetry = {},
            retryButtonText = "Retry",
        )
    )
}