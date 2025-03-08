package com.example.ui.common.content


data class ContentErrorConfig(
    val errorTitle: String? = null,
    val errorSubTitle: String? = null,
    val onRetry: (() -> Unit)? = null
)