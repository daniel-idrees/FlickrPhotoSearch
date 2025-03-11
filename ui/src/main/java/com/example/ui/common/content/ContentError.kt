package com.example.ui.common.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.ui.R
import com.example.ui.common.SPACING_EXTRA_LARGE
import com.example.ui.common.SPACING_MEDIUM

@Composable
internal fun ContentError(
    contentErrorConfig: ContentErrorConfig,
    modifier: Modifier = Modifier,
    titleStyle: TextStyle = MaterialTheme.typography.headlineSmall,
    subtitleStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    subTitleMaxLines: Int = Int.MAX_VALUE,
) {
    val errorTitleText = when (contentErrorConfig.errorTitleRes) {
        is ContentErrorConfig.ErrorMessage.Resource -> stringResource(contentErrorConfig.errorTitleRes.resId)
        is ContentErrorConfig.ErrorMessage.Text -> contentErrorConfig.errorTitleRes.text
    }

    val errorSubTitleText = when (contentErrorConfig.errorSubTitleRes) {
        is ContentErrorConfig.ErrorMessage.Resource -> stringResource(contentErrorConfig.errorSubTitleRes.resId)
        is ContentErrorConfig.ErrorMessage.Text -> contentErrorConfig.errorSubTitleRes.text
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(SPACING_MEDIUM.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = errorTitleText,
            style = titleStyle,
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = errorSubTitleText,
            style = subtitleStyle,
            maxLines = subTitleMaxLines,
            overflow = TextOverflow.Ellipsis
        )
        if (contentErrorConfig.onRetry != null) {
            Icon(
                modifier = Modifier
                    .padding(top = SPACING_MEDIUM.dp)
                    .size(SPACING_EXTRA_LARGE.dp)
                    .clickable {
                        contentErrorConfig.onRetry.invoke()
                    }
                    .align(Alignment.CenterHorizontally),
                imageVector = Icons.Default.Refresh,
                contentDescription = "Refresh icon on error"
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
@PreviewLightDark
private fun ContentErrorWithErrorResourcePreview() {
    ContentError(
        ContentErrorConfig(
            errorTitleRes = ContentErrorConfig.ErrorMessage.Resource(R.string.main_view_model_generic_error_title),
            errorSubTitleRes = ContentErrorConfig.ErrorMessage.Resource(R.string.main_view_model_generic_error_sub_title),
            onRetry = {}
        )
    )
}

@Composable
@Preview(showBackground = true)
@PreviewLightDark
private fun ContentErrorWithErrorTextPreview() {
    ContentError(
        ContentErrorConfig(
            errorTitleRes = ContentErrorConfig.ErrorMessage.Text("Oops..."),
            errorSubTitleRes = ContentErrorConfig.ErrorMessage.Text("Something went wrong."),
            onRetry = {}
        )
    )
}