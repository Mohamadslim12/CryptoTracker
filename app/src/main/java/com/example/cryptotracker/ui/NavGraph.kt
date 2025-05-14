package com.example.cryptotracker.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.cryptotracker.ui.screens.CryptoDetailScreen
import com.example.cryptotracker.ui.screens.CryptoListScreen
import com.example.cryptotracker.ui.screens.HomeScreen

sealed class Screen(val route: String, val icon: @Composable () -> Unit, val label: String) {
    object Home   : Screen("home",        { Icon(Icons.Filled.Home, contentDescription = null) }, "Home")
    object List   : Screen("crypto_list", { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) }, "All")
    object Detail : Screen("crypto_detail/{cryptoId}", {}, "")
    {
        fun createRoute(id: String) = "crypto_detail/$id"
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun CryptoNavHost(viewModel: CryptoViewModel) {
    val navCtrl      = rememberNavController()
    val allCryptos   by viewModel.cryptos.collectAsState()
    val tracked      by viewModel.trackedCryptos.collectAsState()
    val currentPage  by viewModel.currentPage.collectAsState()
    val totalPages   = viewModel.totalPages
    val backStack    by navCtrl.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                listOf(Screen.Home, Screen.List).forEach { screen ->
                    NavigationBarItem(
                        icon     = screen.icon,
                        label    = { Text(screen.label) },
                        selected = currentRoute == screen.route,
                        onClick  = {
                            navCtrl.navigate(screen.route) {
                                popUpTo(navCtrl.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState    = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController    = navCtrl,
            startDestination = Screen.Home.route,
            modifier         = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    tracked         = tracked,
                    onRemoveTracked = { viewModel.toggleTracked(it) },
                    cryptos         = allCryptos,
                    onSearch        = { q ->
                        allCryptos
                            .firstOrNull { c -> c.name.equals(q, true) || c.symbol.equals(q, true) }
                            ?.let { navCtrl.navigate(Screen.Detail.createRoute(it.id)) }
                    },
                    onItemClick     = { navCtrl.navigate(Screen.Detail.createRoute(it)) }
                )
            }

            composable(Screen.List.route) {
                CryptoListScreen(
                    cryptos     = allCryptos,
                    currentPage = currentPage,
                    totalPages  = totalPages,
                    onPrevPage  = { viewModel.onPrevPage() },
                    onNextPage  = { viewModel.onNextPage() },
                    onItemClick = { navCtrl.navigate(Screen.Detail.createRoute(it)) }
                )
            }

            composable(
                route     = Screen.Detail.route,
                arguments = listOf(navArgument("cryptoId") { type = NavType.StringType })
            ) { entry ->
                val cryptoId = entry.arguments!!.getString("cryptoId")!!
                val coin     = allCryptos.find { it.id == cryptoId }
                if (coin != null) {
                    val isTracked    = tracked.any { it.id == cryptoId }
                    val canTrackMore = isTracked || tracked.size < 5
                    CryptoDetailScreen(
                        coin            = coin,
                        isTracked       = isTracked,
                        canTrackMore    = canTrackMore,
                        onToggleTracked = { viewModel.toggleTracked(cryptoId) },
                        onBackClick     = { navCtrl.popBackStack() }
                    )
                }
            }
        }
    }
}



