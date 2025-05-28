package com.example.ecotracker.data.rules

object Validator {

    fun validateFirstName(firstName: String): ValidationResult {
        return ValidationResult(
            (!firstName.isNullOrEmpty() && firstName.length >= 3)
        )
    }


    fun validateLastName(lastName: String): ValidationResult {
        return ValidationResult(
            (!lastName.isNullOrEmpty() && lastName.length >= 4)
        )
    }

    fun validateEmail(email: String): ValidationResult {
        return ValidationResult(
            (!email.isNullOrEmpty())
        )
    }

    fun validatePassword(password: String): ValidationResult {
        return ValidationResult(
            (!password.isNullOrEmpty() && password.length >= 8)
        )
    }
}

data class ValidationResult(
    val status: Boolean
)