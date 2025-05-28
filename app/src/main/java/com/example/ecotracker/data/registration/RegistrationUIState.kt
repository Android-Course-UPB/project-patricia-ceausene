package com.example.ecotracker.data.registration

data class RegistrationUIState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val registrationSuccess: Boolean = false,
    val registrationError: String = "",
    val firstNameError: Boolean = false,
    val lastNameError: Boolean = false,
    val emailError: Boolean = false,
    val passwordError: Boolean = false,
)