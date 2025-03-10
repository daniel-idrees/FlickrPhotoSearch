package com.example.ui.main.bottomtabs.screen

import android.net.Uri
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

internal const val HOME_ROUTE = "home"
internal const val SEARCH_ROUTE = "search"
internal const val HISTORY_ROUTE = "history"

internal const val HOME_LABEL = "Home"
internal const val SEARCH_LABEL = "Search"
internal const val HISTORY_LABEL = "History"

internal val homeIcon = Icons.Default.Home
internal val searchIcon = Icons.Default.Search
internal val historyIcon = Icons.Default.Refresh

internal sealed class BottomBarScreen(val route: String, val label: String, val tabImageVector: ImageVector) {
    data object Home : BottomBarScreen(HOME_ROUTE, HOME_LABEL, homeIcon)
    data object Search : BottomBarScreen(SEARCH_ROUTE, SEARCH_LABEL, searchIcon)
    data object History : BottomBarScreen(HISTORY_ROUTE, HISTORY_LABEL, historyIcon)
}

internal fun createRouteWithPathArguments(route: String, vararg arguments: String): String {
    val builder = Uri.parse(route).buildUpon()
    arguments.forEach {
        builder.appendEncodedPath("{$it}")
    }
    return builder.build().toString()
}
