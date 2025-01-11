package com.machado001.packagemanagerinteractor.presentation

import android.content.pm.PackageInfo
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.machado001.packagemanagerinteractor.domain.PackageRepository
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(repository: PackageRepository) : ViewModel() {

    var searchQuery by mutableStateOf("")
        private set

    val searchResults: StateFlow<List<PackageInfo>> = snapshotFlow { searchQuery }
        .combine(repository.packageInfos) { query, packages ->
            if (searchQuery.isNotEmpty()) {
                packages.filter { packageInfo ->
                    packageInfo.packageName.contains(query, ignoreCase = true)
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