package com.example.cryptotracker.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.cryptotracker.data.Crypto

@Composable
fun CryptoListScreen(
    cryptos: List<Crypto>,
    currentPage: Int,
    totalPages: Int,
    onNextPage: () -> Unit,
    onPrevPage: () -> Unit,
    onItemClick: (String) -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(cryptos) { coin ->
                Card(
                    modifier  = Modifier
                        .fillMaxWidth()
                        .clickable { onItemClick(coin.id) },
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        AsyncImage(
                            model             = coin.image,
                            contentDescription= coin.name,
                            modifier          = Modifier.size(40.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(coin.name, style = MaterialTheme.typography.titleMedium)
                            Text(
                                text  = "$${coin.current_price}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }

        HorizontalDivider()

        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                enabled = currentPage > 1,
                onClick = onPrevPage
            ) {
                Text("Previous")
            }
            Text("Page $currentPage of $totalPages", style = MaterialTheme.typography.bodyMedium)
            Button(
                enabled = currentPage < totalPages,
                onClick = onNextPage
            ) {
                Text("Next")
            }
        }
    }
}


