package com.example.cryptotracker.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.cryptotracker.data.Crypto

enum class HomeFilter { Change24h, CirculatingSupply, MarketCap }

@Composable
fun HomeScreen(
    tracked: List<Crypto>,
    onRemoveTracked: (String) -> Unit,
    cryptos: List<Crypto>,
    onSearch: (String) -> Unit,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var query by remember { mutableStateOf("") }
    var filter by remember { mutableStateOf(HomeFilter.Change24h) }

    Column(modifier.fillMaxSize().padding(16.dp)) {
        Text("CryptoTracker", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Search…") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch(query.trim()) })
        )
        Spacer(Modifier.height(16.dp))

        if (tracked.isNotEmpty()) {
            Text("Tracked", style = MaterialTheme.typography.titleMedium)
            Row(
                Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(vertical = 8.dp)
            ) {
                tracked.forEach { coin ->
                    Card(
                        Modifier
                            .padding(end = 8.dp)
                            .clickable { onItemClick(coin.id) },
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                model = coin.image,
                                contentDescription = coin.name,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(coin.symbol.uppercase(), style = MaterialTheme.typography.bodyMedium)
                            Spacer(Modifier.width(8.dp))
                            IconButton(onClick = { onRemoveTracked(coin.id) }) {
                                Icon(Icons.Filled.Close, contentDescription = null)
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            HomeFilter.entries.forEach { f ->
                val label = when (f) {
                    HomeFilter.Change24h         -> "24h Δ"
                    HomeFilter.CirculatingSupply -> "Circ. Supply"
                    HomeFilter.MarketCap         -> "Market Cap"
                }
                val colors = if (filter == f) ButtonDefaults.buttonColors() else ButtonDefaults.outlinedButtonColors()
                Button(onClick = { filter = f }, colors = colors) {
                    Text(label)
                }
            }
        }
        Spacer(Modifier.height(16.dp))

        val top5 = remember(cryptos, filter) {
            when (filter) {
                HomeFilter.Change24h ->
                    cryptos.sortedByDescending { it.price_change_percentage_24h ?: 0.0 }
                HomeFilter.CirculatingSupply ->
                    cryptos.sortedByDescending { it.circulating_supply ?: 0.0 }
                HomeFilter.MarketCap ->
                    cryptos.sortedByDescending { it.market_cap ?: 0.0 }
            }.take(5)
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(top5) { coin ->
                Card(
                    Modifier
                        .fillMaxWidth()
                        .clickable { onItemClick(coin.id) },
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = coin.image,
                            contentDescription = coin.name,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(coin.name, style = MaterialTheme.typography.titleMedium)
                            Text(
                                when (filter) {
                                    HomeFilter.Change24h ->
                                        "${"%.2f".format(coin.price_change_percentage_24h ?: 0.0)}%"
                                    HomeFilter.CirculatingSupply ->
                                        "%,.0f".format(coin.circulating_supply ?: 0.0)
                                    HomeFilter.MarketCap ->
                                        "$${"%,.0f".format(coin.market_cap ?: 0.0)}"
                                },
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}





