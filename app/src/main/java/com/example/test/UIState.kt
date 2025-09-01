package com.example.test

sealed class UIState {
    data object Default: UIState()
    data object Loading : UIState()
    data class Success(val data: Any) : UIState()
    data class Error(val message: String) : UIState()

}