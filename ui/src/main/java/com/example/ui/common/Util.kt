package com.example.ui.common

import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

internal const val SPACING_EXTRA_SMALL = 4
internal const val SPACING_SMALL = 8
internal const val SPACING_MEDIUM = 16
internal const val SPACING_LARGE = 24
internal const val SPACING_EXTRA_LARGE = 48


internal fun screenPaddings(
    append: PaddingValues? = null,
) = PaddingValues(
    start = SPACING_LARGE.dp,
    top = SPACING_EXTRA_LARGE.dp + (append?.calculateTopPadding() ?: 0.dp),
    end = SPACING_LARGE.dp,
    bottom = SPACING_LARGE.dp + (append?.calculateBottomPadding() ?: 0.dp)
)

internal fun Context.getActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

internal fun showErrorToast(context: Context, message: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(
        context,
        message,
        duration,
    ).show()
}


@Composable
fun keyboardAsState(): State<Boolean> {
    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    return rememberUpdatedState(isImeVisible)
}
