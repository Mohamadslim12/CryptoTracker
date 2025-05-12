package com.example.cryptotracker.data

data class Crypto(
    val id: String,
    val symbol: String,
    val name: String,
    val image: String,

    val current_price: Double,
    val price_change_percentage_24h: Double?,

    val high_24h: Double?,
    val low_24h: Double?,

    val market_cap: Double?,
    val market_cap_rank: Int?,

    val circulating_supply: Double?,
    val total_supply: Double?,

    val ath: Double?,
    val atl: Double?
)
