package com.machado001.packagemanagerinteractor.presentation

sealed interface MainScreenAction {
    data class OnUserSearch(val query: String) : MainScreenAction
    data object OnUserNavigateToPackageDetails : MainScreenAction
}