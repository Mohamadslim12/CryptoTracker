package com.example.cryptotracker.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.cryptotracker.data.Crypto

enum class HomeFilter { Change24h, MostExpensive, MarketCap }

@Composable
fun HomeScreen(
    cryptos: List<Crypto>,
    onSearch: (String) -> Unit,
    onItemClick: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var filter by remember { mutableStateOf(HomeFilter.Change24h) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
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

        Spacer(Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            HomeFilter.values().forEach { f ->
                val label = when (f) {
                    HomeFilter.Change24h     -> "24h Δ"
                    HomeFilter.MostExpensive -> "Price"
                    HomeFilter.MarketCap     -> "Market Cap"
                }
                val colors = if (filter == f)
                    ButtonDefaults.buttonColors()
                else
                    ButtonDefaults.outlinedButtonColors()
                Button(onClick = { filter = f }, colors = colors) {
                    Text(label)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        val top5 = remember(cryptos, filter) {
            when (filter) {
                HomeFilter.Change24h     -> cryptos.sortedByDescending { it.price_change_percentage_24h ?: 0.0 }
                HomeFilter.MostExpensive -> cryptos.sortedByDescending { it.current_price }
                HomeFilter.MarketCap     -> cryptos.sortedByDescending { it.market_cap ?: 0.0 }
            }.take(5)
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(top5) { coin ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onItemClick(coin.id) },
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(12.dp)
                    ) {
                        AsyncImage(
                            model = coin.image,
                            contentDescription = coin.name,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(coin.name, style = MaterialTheme.typography.titleMedium)
                            Text(
                                text = when (filter) {
                                    HomeFilter.Change24h     ->
                                        "${"%.2f".format(coin.price_change_percentage_24h ?: 0.0)}%"
                                    HomeFilter.MostExpensive ->
                                        "$${"%,.2f".format(coin.current_price)}"
                                    HomeFilter.MarketCap     ->
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



