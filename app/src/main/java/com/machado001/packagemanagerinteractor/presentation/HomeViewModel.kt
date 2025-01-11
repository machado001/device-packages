package com.machado001.packagemanagerinteractor.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.machado001.packagemanagerinteractor.domain.Package
import com.machado001.packagemanagerinteractor.domain.PackageRepository
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(repository: PackageRepository) : ViewModel() {

    var searchQuery by mutableStateOf("")
        private set

    val searchResults: StateFlow<List<Package>> = snapshotFlow { searchQuery }
        .combine(repository.packageInfos) { query, packages ->
            if (searchQuery.isNotEmpty()) {
                packages.filter { pkg ->
                    pkg.name.contains(query, ignoreCase = true)
                }
            } else packages
        }
        .stateIn(
            viewModelScope,
            initialValue = emptyList(),
            started = WhileSubscribed(5000)
        )

    fun onSearch(query: String) {
        searchQuery = query
    }
}