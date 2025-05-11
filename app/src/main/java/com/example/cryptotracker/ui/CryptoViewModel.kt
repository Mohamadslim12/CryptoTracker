package com.example.cryptotracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptotracker.data.Crypto
import com.example.cryptotracker.repository.CryptoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CryptoViewModel(private val repo: CryptoRepository) : ViewModel() {
    private val _cryptos = MutableStateFlow<List<Crypto>>(emptyList())
    val cryptos: StateFlow<List<Crypto>> = _cryptos

    init { loadCryptos() }

    fun loadCryptos() {
        viewModelScope.launch {
            _cryptos.value = repo.fetchCryptos()
        }
    }
}
