package com.example.cryptotracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.cryptotracker.data.Crypto

@Composable
fun CryptoDetailScreen(
    coin: Crypto,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = coin.name,
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Spacer(Modifier.height(24.dp))


        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = coin.image,
                contentDescription = coin.name,
                modifier = Modifier.size(100.dp)
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = coin.symbol.uppercase(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "$${coin.current_price}",
                style = MaterialTheme.typography.headlineSmall
            )
        }

        Spacer(Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                StatRow("24h Change", "${"%.2f".format(coin.price_change_percentage_24h ?: 0.0)}%")
                StatRow("High (24h)", "$${coin.high_24h ?: "—"}")
                StatRow("Low (24h)", "$${coin.low_24h ?: "—"}")
                StatRow("Market Cap", "$${coin.market_cap?.let { "%,.0f".format(it) } ?: "—"}")
                StatRow("Total Supply", coin.total_supply?.let { "%,.0f".format(it) } ?: "—")
                StatRow("Circulating Supply", coin.circulating_supply?.let { "%,.0f".format(it) } ?: "—")
                StatRow("All-Time High", "$${coin.ath ?: "—"}")
                StatRow("All-Time Low", "$${coin.atl ?: "—"}")
            }
        }
    }
}

@Composable
fun StatRow(label: String, value: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}
