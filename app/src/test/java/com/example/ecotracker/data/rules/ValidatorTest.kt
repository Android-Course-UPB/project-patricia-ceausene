package com.example.ecotracker.data.rules

import org.junit.Assert.*
import org.junit.Test

class ValidatorTest {
    @Test
    fun validateFirstName_ValidFirstName_ReturnsTrue() {
        val result = Validator.validateFirstName("John")
        assertEquals(true, result.status)
    }
    @Test
    fun validateFirstName_EmptyFirstName_ReturnsFalse() {
        val result = Validator.validateFirstName("")
        assertEquals(false, result.status)
    }
    @Test
    fun validateFirstName_ShortFirstName_ReturnsFalse() {
        val result = Validator.validateFirstName("Jo")
        assertEquals(false, result.status)
    }
    @Test
    fun validateLastName_ValidLastName_ReturnsTrue() {
        val result = Validator.validateLastName("Smith")
        assertEquals(true, result.status)
    }
    @Test
    fun validateLastName_EmptyLastName_ReturnsFalse() {
        val result = Validator.validateLastName("")
        assertEquals(false, result.status)
    }
    @Test
    fun validateLastName_ShortLastName_ReturnsFalse() {
        val result = Validator.validateLastName("Smi")
        assertEquals(false, result.status)
    }
    @Test
    fun validateEmail_NonEmptyEmail_ReturnsTrue() {
        val result = Validator.validateEmail("test@example.com")
        assertEquals(true, result.status)
    }
    @Test
    fun validateEmail_EmptyEmail_ReturnsFalse() {
        val result = Validator.validateEmail("")
        assertEquals(false, result.status)
    }

    @Test
    fun validatePassword_ValidPassword_ReturnsTrue() {
        val result = Validator.validatePassword("password123")
        assertEquals(true, result.status)
    }

    @Test
    fun validatePassword_EmptyPassword_ReturnsFalse() {
        val result = Validator.validatePassword("")
        assertEquals(false, result.status)
    }

    @Test
    fun validatePassword_ShortPassword_ReturnsFalse() {
        val result = Validator.validatePassword("pass")
        assertEquals(false, result.status)
    }
}