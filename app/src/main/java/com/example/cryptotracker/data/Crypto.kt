package com.example.cryptotracker.data

data class Crypto(
    val id: String,
    val symbol: String,
    val name: String,
    val current_price: Double,
    val image: String,
    val price_change_percentage_24h: Double
)
