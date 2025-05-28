package com.example.ecotracker.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ecotracker.R
import com.example.ecotracker.components.AppBottomNavigation
import com.example.ecotracker.components.ButtonComponent
import com.example.ecotracker.components.WeeklyTipComponent
import com.example.ecotracker.data.home.HomeViewModel
import com.example.ecotracker.ui.theme.Primary
import com.example.ecotracker.ui.theme.Tertiary

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = viewModel()
) {
    val weeklyTip = homeViewModel.weeklyTip.collectAsState()

    Surface(
        color = Tertiary,
        modifier = Modifier
            .fillMaxSize()
            .background(Tertiary)
    ) {
        Scaffold(
            bottomBar = {
                AppBottomNavigation(navController)
            },
            containerColor = Tertiary,
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .background(Primary, shape = RoundedCornerShape(20.dp))
                        .fillMaxWidth()
                        .height(30.dp) // Specify the height to make the Box slimmer
                        .padding(horizontal = 16.dp), // Apply only horizontal padding,
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Week ${weeklyTip.value?.week}",
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.weight(1f)
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.plasticfree),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.height(220.dp)

                )
                WeeklyTipComponent(weeklyTip = weeklyTip.value)
                Spacer(modifier = Modifier.height(10.dp))
                ButtonComponent(
                    value = "Calculate your footprint",
                    onButtonClicked = {
                        navController.navigate("calculator")
                    }
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}
