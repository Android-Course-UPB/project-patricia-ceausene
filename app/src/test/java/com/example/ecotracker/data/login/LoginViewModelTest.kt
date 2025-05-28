package com.example.ecotracker.data.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var loginViewModel: LoginViewModel


    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        loginViewModel = LoginViewModel()
    }

    @Test
    fun emailChanged_OnEvent_EmailUpdatedInUIState() {
        val email = "test@example.com"
        loginViewModel.onEvent(LoginUIEvent.EmailChanged(email))

        assertEquals(email, loginViewModel.loginUIState.value.email)
    }

    @Test
    fun passwordChanged_OnEvent_PasswordUpdatedInUIState() {
        val password = "password123"
        loginViewModel.onEvent(LoginUIEvent.PasswordChanged(password))

        assertEquals(password, loginViewModel.loginUIState.value.password)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
