package com.example.cryptotracker.network

import com.example.cryptotracker.data.Crypto
import retrofit2.http.GET
import retrofit2.http.Query

interface CryptoApiService {
    @GET("coins/markets")
    suspend fun getCryptos(
        @Query("vs_currency") vsCurrency: String = "usd",
        @Query("order") order: String = "market_cap_desc",
        @Query("per_page") perPage: Int = 50,
        @Query("page") page: Int = 1,
        @Query("sparkline") sparkline: Boolean = false
    ): List<Crypto>
}
