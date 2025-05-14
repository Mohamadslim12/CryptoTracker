package com.example.cryptotracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptotracker.data.Crypto
import com.example.cryptotracker.repository.CryptoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

class CryptoViewModel(private val repo: CryptoRepository) : ViewModel() {
    private val _cryptos = MutableStateFlow<List<Crypto>>(emptyList())
    val cryptos: StateFlow<List<Crypto>> = _cryptos.asStateFlow()

    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()
    val totalPages = 5

    private val _trackedIds = MutableStateFlow<Set<String>>(emptySet())
    val trackedIds: StateFlow<Set<String>> = _trackedIds.asStateFlow()

    val trackedCryptos: StateFlow<List<Crypto>> = combine(_cryptos, _trackedIds) { list, ids ->
        list.filter { it.id in ids }
    }
        .map { it.take(5) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    init {
        viewModelScope.launch {
            val pages = (1..totalPages).map { page ->
                repo.fetchCryptos(page)
            }
            _cryptos.value = pages.flatten()
        }
    }

    fun onNextPage() {
        if (_currentPage.value < totalPages) {
            _currentPage.value += 1
        }
    }

    fun onPrevPage() {
        if (_currentPage.value > 1) {
            _currentPage.value -= 1
        }
    }

    fun toggleTracked(id: String) {
        val current = _trackedIds.value.toMutableSet()
        if (current.contains(id)) {
            current.remove(id)
        } else if (current.size < 5) {
            current.add(id)
        }
        _trackedIds.value = current
    }
}

