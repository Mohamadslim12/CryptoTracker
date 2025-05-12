package com.example.cryptotracker.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cryptotracker.ui.screens.CryptoDetailScreen
import com.example.cryptotracker.ui.screens.CryptoListScreen
import com.example.cryptotracker.ui.screens.HomeScreen

sealed class Screen(val route: String) {
    object Home   : Screen("home")
    object List   : Screen("crypto_list")
    object Detail : Screen("crypto_detail/{cryptoId}") {
        fun createRoute(id: String) = "crypto_detail/$id"
    }
}

@Composable
fun CryptoNavHost(viewModel: CryptoViewModel) {
    val nav = rememberNavController()
    val cryptos by viewModel.cryptos.collectAsState(initial = emptyList())

    NavHost(navController = nav, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(
                cryptos     = cryptos,
                onSearch    = { q ->
                    cryptos.firstOrNull { it.name.equals(q, true) || it.symbol.equals(q, true) }
                        ?.let { nav.navigate(Screen.Detail.createRoute(it.id)) }
                },
                onItemClick = { id ->
                    nav.navigate(Screen.Detail.createRoute(id))
                },
                onViewAll   = {
                    nav.navigate(Screen.List.route)
                }
            )
        }
        composable(Screen.List.route) {
            CryptoListScreen(
                cryptos     = cryptos,
                onItemClick = { id ->
                    nav.navigate(Screen.Detail.createRoute(id))
                },
                onBack      = { nav.popBackStack() }
            )
        }
        composable(
            route     = Screen.Detail.route,
            arguments = listOf(navArgument("cryptoId") { type = NavType.StringType })
        ) { backStackEntry ->
            val id   = backStackEntry.arguments!!.getString("cryptoId")!!
            val coin = cryptos.find { it.id == id }!!
            CryptoDetailScreen(
                coin        = coin,
                onBackClick = { nav.popBackStack() }
            )
        }
    }
}



