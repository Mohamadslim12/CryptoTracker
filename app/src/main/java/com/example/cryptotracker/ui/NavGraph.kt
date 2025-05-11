package com.example.cryptotracker.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cryptotracker.data.Crypto
import com.example.cryptotracker.ui.screens.CryptoDetailScreen
import com.example.cryptotracker.ui.screens.CryptoListScreen

sealed class Screen(val route: String) {
    object List : Screen("crypto_list")
    object Detail : Screen("crypto_detail/{cryptoId}") {
        fun createRoute(id: String) = "crypto_detail/$id"
    }
}

@Composable
fun CryptoNavHost(viewModel: CryptoViewModel) {
    val navCtrl = rememberNavController()
    NavHost(navCtrl, startDestination = Screen.List.route) {
        composable(Screen.List.route) {
            CryptoListScreen(
                cryptos     = viewModel.cryptos.collectAsState().value,
                onItemClick = { id -> navCtrl.navigate(Screen.Detail.createRoute(id)) }
            )
        }
        composable(
            route     = Screen.Detail.route,
            arguments = listOf(navArgument("cryptoId") { type = NavType.StringType })
        ) { backStackEntry ->
            val id     = backStackEntry.arguments?.getString("cryptoId")!!
            val crypto = viewModel.cryptos.collectAsState().value.find { it.id == id }
            crypto?.let {
                CryptoDetailScreen(
                    coin        = it,
                    onBackClick = { navCtrl.popBackStack() }
                )
            }
        }
    }
}
