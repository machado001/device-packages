package com.machado001.packagemanagerinteractor.presentation

import android.content.pm.PackageInfo
import androidx.compose.foundation.text.input.TextFieldState

data class MainScreenUiState(
    val packages: List<PackageInfo> = emptyList(),
    val intrinsicState: IntrinsicState = IntrinsicState.Loading,
    val searchQuery: String = ""
)

sealed interface IntrinsicState {
    data object Loading : IntrinsicState
    data object Success : IntrinsicState
    data object Error : IntrinsicState
}
