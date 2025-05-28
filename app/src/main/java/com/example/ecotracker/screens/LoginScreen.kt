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
import com.example.ecotracker.components.UnderlinedTextComponent
import com.example.ecotracker.data.login.LoginUIEvent
import com.example.ecotracker.data.login.LoginViewModel
import com.example.ecotracker.ui.theme.Primary

@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = viewModel()
) {

    val loginState = loginViewModel.loginUIState.collectAsState().value

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
                HeadingTextComponent(value = stringResource(R.string.welcome_back))
                Spacer(modifier = Modifier.height(20.dp))
                if (loginState.loginError.isNotEmpty()) {
                    Text(
                        text = "The credentials you entered are incorrect. Please try again.",
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                }
                MyTextField(
                    label = stringResource(R.string.email),
                    painterResource = painterResource(R.drawable.mail),
                    onTextSelected = {
                        loginViewModel.onEvent(LoginUIEvent.EmailChanged(it))
                    },
                    errorStatus = loginState.emailError
                )
                PasswordTextField(
                    label = stringResource(R.string.password),
                    painterResource = painterResource(R.drawable.lock),
                    onTextSelected = {
                        loginViewModel.onEvent(LoginUIEvent.PasswordChanged(it))
                    },
                    errorStatus = loginState.passwordError
                )
                Spacer(modifier = Modifier.height(40.dp))
                UnderlinedTextComponent(value = stringResource(R.string.forgot_your_password))
                Spacer(modifier = Modifier.height(40.dp))
                ButtonComponent(
                    value = stringResource(R.string.login),
                    onButtonClicked = {
                        loginViewModel.onEvent(LoginUIEvent.LoginButtonClicked)
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))
                DividerTextComponent()
                ClickableLoginTextComponent(tryingToLogin = false, onTextSelected = {
                    navController.navigate("signup")
                })
            }
        }

        if (loginState.isLoading) {
            CircularProgressIndicator(
                color = Primary
            )
        }

        if (loginState.loginSuccess) {
            LaunchedEffect(Unit) {
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }
    }
}