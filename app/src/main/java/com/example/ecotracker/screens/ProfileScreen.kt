package com.example.ecotracker.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarStyle
import co.yml.charts.ui.barchart.models.SelectionHighlightData
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.ecotracker.R
import com.example.ecotracker.components.AppBottomNavigation
import com.example.ecotracker.data.user.UserViewModel
import com.example.ecotracker.model.EntryCarbonFootprint
import com.example.ecotracker.ui.theme.Primary
import com.example.ecotracker.ui.theme.Secondary
import com.example.ecotracker.ui.theme.Tertiary

@Composable
fun ProfileScreen(
    navController: NavController,
    userViewModel: UserViewModel = viewModel()
) {
    Scaffold(
        topBar = { UserProfileTopBar(navController, userViewModel) },
        containerColor = Tertiary,
        bottomBar = {
            AppBottomNavigation(navController)
        }
    ) { innerPadding ->
        ProfileContent(Modifier.padding(innerPadding), userViewModel)
    }
}

@Composable
fun ProfileContent(
    modifier: Modifier,
    userViewModel: UserViewModel
) {
    val isLoading by userViewModel.isLoading.collectAsState()
    val user by userViewModel.currentUser.collectAsState()
    if (isLoading) {
        CircularProgressIndicator()
    } else {
        Column(
            modifier = modifier
                .padding(8.dp)
                .fillMaxWidth()
                .background(Tertiary)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            ProfileImage(userViewModel)
            user?.let { Statistics(weight = it.weight, points = it.points) }
            CarbonHistoryScreen(user?.carbonFootprints ?: listOf() )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun CarbonHistoryScreen(carbonFootprints: List<EntryCarbonFootprint>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Carbon history",
            color = Primary,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            modifier = Modifier.padding(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
        ) {
            if(carbonFootprints.size < 2) {
                Text("Not enough data yet")
            } else {
                MyBarChart(carbonFootprints)
            }
        }
    }
}

@Composable
fun MyBarChart(carbonFootprints: List<EntryCarbonFootprint>) {

    val barData = carbonFootprints.mapIndexed { index, entry ->
        Point(index.toFloat() + 1, entry.carbonFootprint.totalCarbonFootprint.toFloat())
    }.map { entry -> BarData(point = entry, label = carbonFootprints[entry.x.toInt() - 1].date, color = Primary) }


    val xAxisData = AxisData.Builder()
        .axisStepSize(30.dp)
        .steps(barData.size - 1)
        .bottomPadding(40.dp)
        .axisLabelAngle(20f)
        .startDrawPadding(48.dp)
        .labelData { index -> barData[index].label }
        .build()

    val yAxisData = AxisData.Builder()
        .steps(barData.size - 1)
        .labelAndAxisLinePadding(20.dp)
        .axisOffset(20.dp)
        .build()
    val barChartData = BarChartData(
        chartData = barData,
        barStyle = BarStyle(
            paddingBetweenBars = 20.dp,
            barWidth = 25.dp,
            selectionHighlightData = SelectionHighlightData(popUpLabel = { _, y -> "${String.format("%.2f", y)} tons CO2"})
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        showXAxis = true,
        horizontalExtraSpace = 10.dp,
    )
    BarChart(modifier = Modifier.height(300.dp), barChartData = barChartData)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileTopBar(
    navController: NavController,
    userViewModel: UserViewModel
) {
    TopAppBar(
        title = { },
        actions = {
            SmallFloatingActionButton(
            onClick = {
                userViewModel.logout()
                navController.navigate("login")
            },
            containerColor = Primary,
            contentColor = Secondary,
        ) { Icon(Icons.Filled.ExitToApp, null) }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Tertiary)
    )
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ProfileImage(userViewModel: UserViewModel) {

    val showNotification by userViewModel.showUpdatedProfileImage.collectAsState()
    if (showNotification) {
        Toast.makeText(LocalContext.current,
            stringResource(R.string.you_successfully_updated_your_profile_image), Toast.LENGTH_LONG).show()
        userViewModel.resetShowUpdatedProfileImage()
    }

    val imageUri by userViewModel.imageUri.collectAsState()
    val painter = rememberImagePainter(
        if (imageUri.isEmpty())
            R.drawable.baseline_person_24
        else
            imageUri
    )

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        uri: Uri? ->
        uri?.let {
            userViewModel.updateUserProfileImage(it)
        }
    }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = CircleShape,
            modifier = Modifier
                .padding(8.dp)
                .size(160.dp),
            colors = CardDefaults.cardColors(
                containerColor = Secondary
            ),
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .wrapContentSize()
                    .clickable { launcher.launch("image/*") },
                contentScale = ContentScale.Crop,
                colorFilter = if (imageUri.isEmpty()) {
                    ColorFilter.tint(Primary)
                } else {
                    null
                }
            )
        }
        Text(text = "Change profile picture")
    }
}

@Composable
fun Statistics(weight: Float = 0f, points: Int = 0) {
    val annotatedString = buildAnnotatedString {
        append("CO")
        withStyle(
            style = SpanStyle(baselineShift = BaselineShift.Subscript, fontSize = 12.sp)
        ) {
            append("2")
        }
        append("e saved")
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .background(Tertiary)
    ) {
        Text(
            text = "Statistics",
            color = Primary,
            fontWeight = FontWeight.Bold
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
//                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatisticCard(
                painter = painterResource(R.drawable.baseline_cloud_download_24),
                title =  String.format("%.2f", weight) + " kg",
                description = annotatedString
            )
            StatisticCard(
                painter = painterResource(R.drawable.baseline_star_24) ,
                title = points.toString(),
                description = buildAnnotatedString { append("Points") }
            )
        }
    }
}

@Composable
fun StatisticCard(painter: Painter, title: String, description: AnnotatedString) {
    Card(
        modifier = Modifier.padding(8.dp).height(75.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.size(36.dp),
                tint = Primary
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = title,
                    color = Primary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    color = Color.Gray
                )
            }
        }
    }
}