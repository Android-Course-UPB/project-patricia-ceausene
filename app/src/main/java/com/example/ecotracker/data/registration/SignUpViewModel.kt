package com.example.ecotracker.data.registration

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecotracker.data.rules.Validator
import com.example.ecotracker.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SignUpViewModel: ViewModel() {
    private var allValidationsPassed = mutableStateOf(false)
    private val _registrationUIState = MutableStateFlow(RegistrationUIState())
    val registrationUIState: StateFlow<RegistrationUIState> = _registrationUIState


    companion object {
        private const val TAG = "SignUpViewModel"
    }

    fun onEvent(event: SignUpUIEvent) {
        when (event) {
            is SignUpUIEvent.FirstNameChanged -> {
                _registrationUIState.value = registrationUIState.value.copy(
                    firstName = event.firstName
                )
            }
            is SignUpUIEvent.LastNameChanged -> {
                _registrationUIState.value = registrationUIState.value.copy(
                    lastName = event.lastName
                )
            }
            is SignUpUIEvent.EmailChanged -> {
                _registrationUIState.value = registrationUIState.value.copy(
                    email = event.email
                )
            }
            is SignUpUIEvent.PasswordChanged -> {
                _registrationUIState.value = registrationUIState.value.copy(
                    password = event.password
                )
            }
            is SignUpUIEvent.RegisterButtonClicked -> {
                signUp()
            }

        }
    }

    private fun signUp() {
        validateDateWithRules()
        if (allValidationsPassed.value) {
            viewModelScope.launch(Dispatchers.Main) {
                try {
                    _registrationUIState.value = registrationUIState.value.copy(isLoading = true)
                    createUserInFirebase(
                        email = registrationUIState.value.email,
                        password = registrationUIState.value.password,
                        firstName = registrationUIState.value.firstName,
                        lastName = registrationUIState.value.lastName
                    )
                    _registrationUIState.value = registrationUIState.value.copy(isLoading = false)
                    _registrationUIState.value = registrationUIState.value.copy(registrationSuccess = true)
                } catch (e: Exception) {
                   _registrationUIState.value = registrationUIState.value.copy(registrationSuccess = false)
                   _registrationUIState.value = registrationUIState.value.copy(isLoading = false)
                   _registrationUIState.value = registrationUIState.value.copy(registrationError = "${e.message}")
                    Log.d(TAG, "sign up failed: ${e.message}")
                }
            }
        }
    }

    fun validateDateWithRules() {
        val firstNameResult = Validator.validateFirstName(
            firstName = registrationUIState.value.firstName
        )
        val lastNameResult = Validator.validateLastName(
            lastName = registrationUIState.value.lastName
        )
        val emailResult = Validator.validateEmail(
            email = registrationUIState.value.email
        )
        val passwordResult = Validator.validatePassword(
            password = registrationUIState.value.password
        )

        _registrationUIState.value = registrationUIState.value.copy(
            firstNameError = !firstNameResult.status,
            lastNameError = !lastNameResult.status,
            emailError = !emailResult.status,
            passwordError = !passwordResult.status
        )

        allValidationsPassed.value = firstNameResult.status && lastNameResult.status && emailResult.status && passwordResult.status

    }


    private suspend fun createUserInFirebase(email: String, password: String, firstName: String, lastName: String) {
        withContext(Dispatchers.IO) {
            val firebaseAuthResult = FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = firebaseAuthResult.user
            val userId = firebaseUser?.uid ?: throw IllegalStateException("User ID not found")

            val user = User(
                firstName = firstName,
                lastName = lastName,
                email = email
            )

            Firebase.firestore.collection("users").document(userId).set(user).await()
        }
    }

}