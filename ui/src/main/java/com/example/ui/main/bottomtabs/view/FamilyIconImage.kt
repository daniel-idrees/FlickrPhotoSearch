@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.ui.main.bottomtabs.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ui.R


@Composable
internal fun FamilyIconImage(modifier: Modifier = Modifier) {
    val tooltipState = rememberTooltipState()

    TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = {
            Surface(
                modifier = Modifier.padding(top = 8.dp),
                shape = RoundedCornerShape(4.dp),
            ) {
                Text(
                    text = stringResource(R.string.search_screen_result_photo_family_icon_tooltip_text),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        state = tooltipState
    ) {
        Image(
            modifier = modifier,
            painter = painterResource(id = R.drawable.ic_family),
            contentDescription = "Photo family visibility icon"
        )
    }
}
