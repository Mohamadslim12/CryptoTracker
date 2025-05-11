package com.example.cryptotracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.cryptotracker.data.Crypto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CryptoDetailScreen(
    coin: Crypto,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(coin.name) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model             = coin.image,
                contentDescription= coin.name,
                modifier          = Modifier.size(100.dp)
            )
            Spacer(Modifier.height(16.dp))

            Text(
                text  = coin.name,
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text  = "Symbol: ${coin.symbol.uppercase()}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(8.dp))

            Text(
                text  = "Price: $${coin.current_price}",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text  = "24h Change: ${"%.2f".format(coin.price_change_percentage_24h)}%",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

