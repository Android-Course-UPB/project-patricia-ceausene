package com.example.ecotracker.data.login

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecotracker.data.rules.Validator
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LoginViewModel: ViewModel() {

    private var allValidationsPassed = mutableStateOf(false)
    private val _loginUIState = MutableStateFlow(LoginUIState())
    val loginUIState: StateFlow<LoginUIState> = _loginUIState

    companion object {
        private const val TAG = "LoginViewModel"
    }

    fun onEvent(event: LoginUIEvent) {
        when (event) {
            is LoginUIEvent.EmailChanged -> {
                _loginUIState.value = loginUIState.value.copy(email = event.email)
            }

            is LoginUIEvent.PasswordChanged -> {
                _loginUIState.value = loginUIState.value.copy(password = event.password)
            }

            is LoginUIEvent.LoginButtonClicked -> {
                login()
            }
        }
    }

    private fun login() {
        validateLoginUIDataWithRules()
        if (allValidationsPassed.value) {
            viewModelScope.launch(Dispatchers.Main) {
                try {
                    _loginUIState.value = loginUIState.value.copy(isLoading = true)
                    val email = loginUIState.value.email
                    val password = loginUIState.value.password

                    // Perform login operation on IO dispatcher
                    withContext(Dispatchers.IO) {
                        FirebaseAuth.getInstance()
                            .signInWithEmailAndPassword(email, password)
                            .await()
                    }

                    Log.d(TAG, "inside login successful")
                    _loginUIState.value = loginUIState.value.copy(loginSuccess = true)
                } catch (e: Exception) {
                    // Handle failed login
                    _loginUIState.value = loginUIState.value.copy(loginError = "${e.message}")
                    _loginUIState.value = loginUIState.value.copy(isLoading = false)
                    Log.d(TAG, "login failed: ${e.message}")
                    Log.d(TAG, "Exception = ${e.localizedMessage}")
                }
            }
        }
    }

    private fun validateLoginUIDataWithRules() {

        val emailResult = Validator.validateEmail(
            email = loginUIState.value.email
        )
        val passwordResult = Validator.validatePassword(
            password = loginUIState.value.password
        )

        _loginUIState.value = loginUIState.value.copy(
            emailError = !emailResult.status,
            passwordError = !passwordResult.status
        )

        allValidationsPassed.value = emailResult.status && passwordResult.status
    }
}