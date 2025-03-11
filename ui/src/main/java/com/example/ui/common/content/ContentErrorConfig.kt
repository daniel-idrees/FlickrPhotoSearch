package com.example.ui.common.content

import androidx.annotation.StringRes


internal data class ContentErrorConfig(
    val errorTitleRes: ErrorMessage,
    val errorSubTitleRes: ErrorMessage,
    val onRetry: (() -> Unit)? = null
) {
    internal sealed interface ErrorMessage {
        data class Resource(@StringRes val resId: Int) : ErrorMessage
        data class Text(val text: String) : ErrorMessage
    }
}

