package com.example.ecotracker.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ecotracker.screens.ActionsListScreenWithFirestore
import com.example.ecotracker.screens.CalculatorScreen
import com.example.ecotracker.screens.DetailActionScreen
import com.example.ecotracker.screens.HomeScreen
import com.example.ecotracker.screens.LeaderboardScreen
import com.example.ecotracker.screens.LoginScreen
import com.example.ecotracker.screens.ProfileScreen
import com.example.ecotracker.screens.ProgressScreen
import com.example.ecotracker.screens.SignUpScreen
import com.example.ecotracker.screens.SurveyScreen
import com.example.ecotracker.screens.UpdateHabitScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun EcoTrackerApp() {

    val navController = rememberNavController()
    val startDestination = if (FirebaseAuth.getInstance().currentUser != null) "home" else "login"

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {

        NavHost(navController = navController, startDestination = startDestination) {
            composable("login") {
                LoginScreen(navController)
            }
            composable("signup") {
                SignUpScreen(navController)
            }
            composable("home") {
                HomeScreen(navController)
            }
            composable("profile") {
                ProfileScreen(navController)
            }
            composable("actions") {
                ActionsListScreenWithFirestore(navController)
            }
            composable("progress") {
                ProgressScreen(navController)
            }
            composable("leaderboard") {
                LeaderboardScreen(navController)
            }
            composable(
                "detailAction/{actionId}",
                arguments = listOf(navArgument("actionId"){
                    type = NavType.StringType
                })
            ) { backStackEntry ->
                val actionId = backStackEntry.arguments?.getString("actionId")
                DetailActionScreen(navController, actionId)
            }
            composable(
                "updateHabit/{actionId}",
                arguments = listOf(navArgument("actionId"){
                    type = NavType.StringType
                })
            ) {backStackEntry ->
                val actionId = backStackEntry.arguments?.getString("actionId")
                UpdateHabitScreen(actionId, navController)
            }
            composable("calculator") {
                CalculatorScreen(navController)
            }
            composable("survey") {
                SurveyScreen(navController)
            }

        }
    }
}