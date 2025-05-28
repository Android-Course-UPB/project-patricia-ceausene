package com.example.ecotracker.data.login

data class LoginUIState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false,
    val loginError: String = "",
    val emailError: Boolean = false,
    val passwordError: Boolean = false
)
