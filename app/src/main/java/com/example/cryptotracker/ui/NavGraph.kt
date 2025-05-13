package com.example.cryptotracker.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.cryptotracker.ui.screens.CryptoDetailScreen
import com.example.cryptotracker.ui.screens.CryptoListScreen
import com.example.cryptotracker.ui.screens.HomeScreen

sealed class Screen(val route: String, val label: String, val icon: @Composable () -> Unit) {
    object Home   : Screen("home",        "Home", { Icon(Icons.Filled.Home, contentDescription = null) })
    object List   : Screen("crypto_list", "All",  { Icon(Icons.Filled.List, contentDescription = null) })
    object Detail : Screen("crypto_detail/{cryptoId}", "", { }) {
        fun createRoute(id: String) = "crypto_detail/$id"
    }
}

@Composable
fun CryptoNavHost(viewModel: CryptoViewModel) {
    val navCtrl    = rememberNavController()
    val allCryptos by viewModel.cryptos.collectAsState(initial = emptyList())
    val backStack  by navCtrl.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                listOf(Screen.Home, Screen.List).forEach { screen ->
                    NavigationBarItem(
                        icon      = screen.icon,
                        label     = { Text(screen.label) },
                        selected  = currentRoute == screen.route,
                        onClick   = {
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
            navController   = navCtrl,
            startDestination= Screen.Home.route,
            modifier        = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    cryptos     = allCryptos,
                    onSearch    = { query ->
                        allCryptos
                            .firstOrNull { coin ->
                                coin.name.equals(query, ignoreCase = true) ||
                                        coin.symbol.equals(query, ignoreCase = true)
                            }
                            ?.let { found ->
                                navCtrl.navigate(Screen.Detail.createRoute(found.id))
                            }
                    },
                    onItemClick = { id ->
                        navCtrl.navigate(Screen.Detail.createRoute(id))
                    }
                )
            }
            composable(Screen.List.route) {
                CryptoListScreen(
                    cryptos     = allCryptos,
                    onItemClick = { id ->
                        navCtrl.navigate(Screen.Detail.createRoute(id))
                    }
                )
            }
            composable(
                route     = Screen.Detail.route,
                arguments = listOf(navArgument("cryptoId") { type = NavType.StringType })
            ) { backStackEntry ->
                val cryptoId = backStackEntry.arguments!!.getString("cryptoId")!!
                val coin     = allCryptos.find { it.id == cryptoId }
                if (coin != null) {
                    CryptoDetailScreen(
                        coin        = coin,
                        onBackClick = { navCtrl.popBackStack() }
                    )
                }
            }
        }
    }
}


