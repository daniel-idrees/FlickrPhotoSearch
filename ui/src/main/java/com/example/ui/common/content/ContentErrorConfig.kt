package com.example.ui.common.content

import androidx.annotation.StringRes


internal data class ContentErrorConfig(
    @StringRes val errorTitleRes: Int,
    @StringRes val errorSubTitleRes: Int,
    val onRetry: (() -> Unit)? = null
)