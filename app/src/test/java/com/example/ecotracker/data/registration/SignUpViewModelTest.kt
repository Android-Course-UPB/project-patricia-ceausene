package com.example.ecotracker.data.registration

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SignUpViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var signUpViewModel: SignUpViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var firebaseAuth: FirebaseAuth

    @Mock
    private lateinit var authResult: AuthResult

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        signUpViewModel = SignUpViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun validateFirstName_FirstNameChanged_StateUpdated() {
        signUpViewModel.onEvent(SignUpUIEvent.FirstNameChanged("John"))
        assertEquals("John", signUpViewModel.registrationUIState.value.firstName)
    }

    @Test
    fun validateLastName_LastNameChanged_StateUpdated() {
        signUpViewModel.onEvent(SignUpUIEvent.LastNameChanged("Doe"))
        assertEquals("Doe", signUpViewModel.registrationUIState.value.lastName)
    }

    @Test
    fun validateEmail_EmailChanged_StateUpdated() {
        signUpViewModel.onEvent(SignUpUIEvent.EmailChanged("john.doe@example.com"))
        assertEquals("john.doe@example.com", signUpViewModel.registrationUIState.value.email)
    }

    @Test
    fun validatePassword_PasswordChanged_StateUpdated() {
        signUpViewModel.onEvent(SignUpUIEvent.PasswordChanged("password123"))
        assertEquals("password123", signUpViewModel.registrationUIState.value.password)
    }

    @Test
    fun validateDateWithRules_ValidInput_NoErrors() {
        signUpViewModel.onEvent(SignUpUIEvent.FirstNameChanged("John"))
        signUpViewModel.onEvent(SignUpUIEvent.LastNameChanged("Smith"))
        signUpViewModel.onEvent(SignUpUIEvent.EmailChanged("john.smith@example.com"))
        signUpViewModel.onEvent(SignUpUIEvent.PasswordChanged("password1"))

        signUpViewModel.validateDateWithRules()

        assertEquals(false, signUpViewModel.registrationUIState.value.firstNameError)
        assertEquals(false, signUpViewModel.registrationUIState.value.lastNameError)
        assertEquals(false, signUpViewModel.registrationUIState.value.emailError)
        assertEquals(false, signUpViewModel.registrationUIState.value.passwordError)
    }

//    @Test
//    fun signUp_ValidInput_SuccessfulRegistration() = runTest {
//        // Mock successful Firebase authentication
//        val authResultTask: Task<AuthResult> = Tasks.forResult(authResult)
//        whenever(firebaseAuth.createUserWithEmailAndPassword(any(), any())).thenReturn(authResultTask)
//        whenever(authResult.user).thenReturn(mock())
//        whenever(authResult.user?.uid).thenReturn("testUserId")
//
//        signUpViewModel.onEvent(SignUpUIEvent.FirstNameChanged("John"))
//        signUpViewModel.onEvent(SignUpUIEvent.LastNameChanged("Smith"))
//        signUpViewModel.onEvent(SignUpUIEvent.EmailChanged("john.smith@example.com"))
//        signUpViewModel.onEvent(SignUpUIEvent.PasswordChanged("password1"))
//
//        signUpViewModel.onEvent(SignUpUIEvent.RegisterButtonClicked)
//
//        advanceUntilIdle()
//
//        assertEquals(false, signUpViewModel.registrationUIState.value.isLoading)
//        assertEquals(true, signUpViewModel.registrationUIState.value.registrationSuccess)
//    }
//
//    @Test
//    fun signUp_InvalidInput_RegistrationFails() = runTest {
//        // Mock unsuccessful Firebase authentication
//        val exception = RuntimeException("Auth failed")
//        val authResultTask: Task<AuthResult> = Tasks.forException(exception)
//        whenever(firebaseAuth.createUserWithEmailAndPassword(any(), any())).thenReturn(authResultTask)
//
//        signUpViewModel.onEvent(SignUpUIEvent.FirstNameChanged("John"))
//        signUpViewModel.onEvent(SignUpUIEvent.LastNameChanged("Doe"))
//        signUpViewModel.onEvent(SignUpUIEvent.EmailChanged("john.doe@example.com"))
//        signUpViewModel.onEvent(SignUpUIEvent.PasswordChanged("password1"))
//
//        signUpViewModel.onEvent(SignUpUIEvent.RegisterButtonClicked)
//
//        advanceUntilIdle()
//
//        assertEquals(false, signUpViewModel.registrationUIState.value.isLoading)
//        assertEquals(false, signUpViewModel.registrationUIState.value.registrationSuccess)
//        assertEquals("Sign up failed: Auth failed", signUpViewModel.registrationUIState.value.registrationError)
//    }
}