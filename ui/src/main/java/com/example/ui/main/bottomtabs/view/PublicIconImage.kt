@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.ui.main.bottomtabs.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.ui.R
import com.example.ui.common.SPACING_LARGE
import com.example.ui.common.SPACING_SMALL

@Composable
internal fun PublicIconImage(isPublic: Boolean) {
    val publicPainterResource =
        painterResource(id = if (!isPublic) R.drawable.ic_private else R.drawable.ic_public)
    val tooltipState = rememberTooltipState()


    TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = {
            Surface(
                modifier = Modifier.padding(top = 8.dp),
                shape = RoundedCornerShape(4.dp),
            ) {
                Text(
                    text = if(isPublic) "Public" else "Locked",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        state = tooltipState
    ) {
        Image(
            modifier = Modifier.size(SPACING_LARGE.dp),
            painter = publicPainterResource,
            contentDescription = "Photo public icon",
        )
    }
}