package com.machado001.packagemanagerinteractor.presentation

sealed interface MainScreenEvent {
    data class Error(val error: UiError) : MainScreenEvent
    data object OnLoadData : MainScreenEvent
}

class UiError
