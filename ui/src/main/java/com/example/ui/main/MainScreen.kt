@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.ui.main

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.common.getActivity
import com.example.ui.common.showToast
import com.example.ui.main.bottomtabs.screen.HomeScreen
import com.example.ui.main.bottomtabs.screen.SearchHistoryScreen
import com.example.ui.main.bottomtabs.screen.SearchScreen
import com.example.ui.main.bottomtabs.screen.config.BottomBarScreen


@Composable
internal fun MainScreen(viewModel: MainViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val bottomNavigationController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(bottomNavigationController) }
    ) { padding ->
        NavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .windowInsetsPadding(WindowInsets.displayCutout),
            navController = bottomNavigationController,
            startDestination = BottomBarScreen.Home.route
        ) {
            composable(BottomBarScreen.Home.route) {
                HomeScreen(
                    viewModel = viewModel
                )
            }
            composable(BottomBarScreen.Search.route) {
                SearchScreen(
                    mainViewModel = viewModel
                )
            }
            composable(BottomBarScreen.History.route) {
                SearchHistoryScreen(
                    viewModel = viewModel
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MainUiEffect.Navigation.SwitchScreen -> bottomNavigationController.navigate(
                    effect.toScreen.route
                ) {
                    popUpTo(bottomNavigationController.graph.startDestinationId)
                    launchSingleTop = true
                }

                MainUiEffect.Navigation.Finish -> context.getActivity()?.finish()
                is MainUiEffect.Navigation.Pop -> {
                    when (effect.fromScreen) {

                        BottomBarScreen.Search, BottomBarScreen.History -> bottomNavigationController.navigate(
                            BottomBarScreen.Home.route
                        ) {
                            popUpTo(bottomNavigationController.graph.startDestinationId)
                            launchSingleTop = true
                        }

                        BottomBarScreen.Home -> context.getActivity()?.finish()
                    }
                }

                is MainUiEffect.ShowEmptyTextError -> showToast(
                    context = context,
                    messageRes = effect.errorMessageRes
                )
            }
        }
    }
}

@Composable
private fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Search,
        BottomBarScreen.History
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        screen.tabImageVector,
                        contentDescription = "${screen.label} bottom bar icon"
                    )
                },
                label = { Text(text = screen.label) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}