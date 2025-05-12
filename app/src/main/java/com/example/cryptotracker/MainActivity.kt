package com.example.cryptotracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.cryptotracker.network.RetrofitBuilder
import com.example.cryptotracker.repository.CryptoRepository
import com.example.cryptotracker.ui.CryptoNavHost
import com.example.cryptotracker.ui.CryptoViewModel
import com.example.cryptotracker.ui.theme.CryptoTrackerTheme

class MainActivity : ComponentActivity() {
    private val viewModel by lazy {
        CryptoViewModel(
            CryptoRepository(RetrofitBuilder.apiService)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CryptoTrackerTheme {
                CryptoNavHost(viewModel)
            }
        }
    }
}

