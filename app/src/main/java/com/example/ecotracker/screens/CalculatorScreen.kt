package com.example.ecotracker.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ecotracker.R
import com.example.ecotracker.components.RoundedProgressBar
import com.example.ecotracker.data.survey.CalculatorViewModel
import com.example.ecotracker.model.CarbonFootprint
import com.example.ecotracker.ui.theme.Primary

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CalculatorScreen(
    navController: NavController,
    calculatorViewModel: CalculatorViewModel = viewModel()
) {
    val carbonFootprint by calculatorViewModel.carbonFootprint.collectAsState()
    val isLoading by calculatorViewModel.isLoading.collectAsState()

    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Primary, textDecoration = TextDecoration.Underline, fontWeight = FontWeight.Medium)) {
            append("Update survey")
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        topBar = {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
                    .height(200.dp)
                    .background(Color.White)
                    .fillMaxWidth()
            ) {
                SmallFloatingActionButton(
                    onClick = {
                        navController.navigate("home")
                    },
                    containerColor = Color.White,
                    contentColor = Primary,
                ) {
                    Icon(Icons.Filled.KeyboardArrowLeft, null)
                }
                Image(
                    painter = painterResource(id = R.drawable.co2calculator),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(190.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .align(Alignment.Center)
                )

            }
        }
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(Color.White)
                    .padding(10.dp)
            ) {
                Spacer(modifier = Modifier.height(200.dp))
                if (carbonFootprint != null ) {
                    ResultReport(carbonFootprint!!.carbonFootprint)
                    Spacer(modifier = Modifier.height(20.dp))
                    ClickableText(
                        text = annotatedString,
                        onClick = {navController.navigate("survey")},
                        modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    SurveyCard(
                        title = "Find your carbon footprint",
                        iconRes = R.drawable.house,
                        actionText = "Take the survey"
                    ) { navController.navigate("survey") }
                }
            }
        }
    }
}

@Composable
fun SurveyCard(title: String, iconRes: Int, actionText: String, onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(2.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = actionText,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun ProgressBarDetail(
    iconId: Int,
    text: String,
    value: Float,
    maxValue: Float
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
        ) {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            RoundedProgressBar(
                progress = value / maxValue,
                modifier = Modifier
                    .height(18.dp)
                    .weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = String.format("%.2f tons", value),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.wrapContentWidth(Alignment.End)
            )
        }
    }
}

@Composable
fun ResultReport(carbonFootprint: CarbonFootprint) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        ProgressBarDetail(iconId = R.drawable.romania, text = "Average in Romania", value = carbonFootprint.averageRomania.toFloat(), maxValue = 20f)
        Spacer(modifier = Modifier.height(24.dp))
        ProgressBarDetail(iconId = R.drawable.baseline_emoji_people_24, text = "Your Carbon footprint", value = carbonFootprint.totalCarbonFootprint.toFloat(), maxValue = 20f)
        Spacer(modifier = Modifier.height(24.dp))
        ProgressBarDetail(iconId = R.drawable.baseline_home_24, text = "Home", value = carbonFootprint.homeCarbonFootprint.toFloat(), maxValue = carbonFootprint.totalCarbonFootprint.toFloat())
        Spacer(modifier = Modifier.height(24.dp))
        ProgressBarDetail(iconId = R.drawable.baseline_directions_car_24, text = "Transport", value = carbonFootprint.transportCarbonFootprint.toFloat(), maxValue = carbonFootprint.totalCarbonFootprint.toFloat())
        Spacer(modifier = Modifier.height(24.dp))
        ProgressBarDetail(iconId = R.drawable.baseline_airplanemode_active_24, text = "Travel", value = carbonFootprint.travelCarbonFootprint.toFloat(), maxValue = carbonFootprint.totalCarbonFootprint.toFloat())
    }
}