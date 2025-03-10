package com.example.ui.common.content


data class ContentErrorConfig(
    val errorTitle: String,
    val errorSubTitle: String,
    val retryButtonText: String? = null,
    val onRetry: (() -> Unit)? = null
)