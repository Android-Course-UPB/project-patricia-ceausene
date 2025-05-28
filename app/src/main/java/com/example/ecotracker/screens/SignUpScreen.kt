package com.example.ecotracker.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ecotracker.R
import com.example.ecotracker.components.ButtonComponent
import com.example.ecotracker.components.ClickableLoginTextComponent
import com.example.ecotracker.components.DividerTextComponent
import com.example.ecotracker.components.HeadingTextComponent
import com.example.ecotracker.components.MyTextField
import com.example.ecotracker.components.NormalTextComponent
import com.example.ecotracker.components.PasswordTextField
import com.example.ecotracker.data.registration.SignUpUIEvent
import com.example.ecotracker.data.registration.SignUpViewModel
import com.example.ecotracker.ui.theme.Primary

@Composable
fun SignUpScreen(
    navController: NavController,
    signUpViewModel: SignUpViewModel = viewModel()
) {

    val registrationState = signUpViewModel.registrationUIState.collectAsState().value

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = Color.White,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(32.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                NormalTextComponent(value = stringResource(R.string.hello))
                HeadingTextComponent(value = stringResource(R.string.create_account))
                Spacer(modifier = Modifier.height(20.dp))
                if (registrationState.registrationError.isNotEmpty()) {
                    Text(
                        text = registrationState.registrationError,
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                }
                MyTextField(
                    label = stringResource(R.string.first_name),
                    painterResource = painterResource(R.drawable.profile),
                    onTextSelected = { signUpViewModel.onEvent(SignUpUIEvent.FirstNameChanged(it)) },
                    errorStatus = registrationState.firstNameError
                )
                MyTextField(
                    label = stringResource(R.string.last_name),
                    painterResource = painterResource(R.drawable.profile),
                    onTextSelected = { signUpViewModel.onEvent(SignUpUIEvent.LastNameChanged(it)) },
                    errorStatus = registrationState.lastNameError
                )
                MyTextField(
                    label = stringResource(R.string.email),
                    painterResource = painterResource(R.drawable.mail),
                    onTextSelected = { signUpViewModel.onEvent(SignUpUIEvent.EmailChanged(it)) },
                    errorStatus = registrationState.emailError
                )
                PasswordTextField(
                    label = stringResource(R.string.password),
                    painterResource = painterResource(R.drawable.lock),
                    onTextSelected = { signUpViewModel.onEvent(SignUpUIEvent.PasswordChanged(it)) },
                    errorStatus = registrationState.passwordError
                )
                Spacer(modifier = Modifier.height(40.dp))
                ButtonComponent(
                    value = stringResource(R.string.register),
                    onButtonClicked = { signUpViewModel.onEvent(SignUpUIEvent.RegisterButtonClicked) }
                )
                Spacer(modifier = Modifier.height(20.dp))
                DividerTextComponent()
                ClickableLoginTextComponent(onTextSelected = {
                    navController.navigate("login")
                })
            }
        }

        if (registrationState.isLoading) {
            CircularProgressIndicator(
                color = Primary
            )
        }

        if (registrationState.registrationSuccess) {
            LaunchedEffect(Unit) {
                navController.navigate("login") {
                    popUpTo("signup") { inclusive = true }
                }
            }
        }
    }
}