package com.example.cryptotracker.repository

import com.example.cryptotracker.data.Crypto
import com.example.cryptotracker.network.CryptoApiService

class CryptoRepository(private val service: CryptoApiService) {
    suspend fun fetchCryptos(): List<Crypto> = service.getCryptos()
}
