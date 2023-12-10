package com.devapps.pamwezi.utils

sealed class UiEvent {
    object popBackStack: UiEvent()
    data class Navigate(val route: String) : UiEvent()
    data class ShowSnackbar(
        val message: String,
        val action: String? = null
    ): UiEvent()
}
