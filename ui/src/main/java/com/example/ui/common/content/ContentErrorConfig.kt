package com.example.ui.common.content


data class ContentErrorConfig(
    val errorTitle: String,
    val errorSubTitle: String,
    val onRetry: (() -> Unit)? = null
)