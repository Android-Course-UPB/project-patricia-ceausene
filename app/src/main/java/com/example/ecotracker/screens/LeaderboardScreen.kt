package com.example.ecotracker.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.ecotracker.R
import com.example.ecotracker.components.AppBottomNavigation
import com.example.ecotracker.components.HeadingTextComponent
import com.example.ecotracker.data.leaderboard.LeaderboardViewModel
import com.example.ecotracker.model.UserLeaderboard
import com.example.ecotracker.ui.theme.Tertiary

@Composable
fun LeaderboardScreen(
    navController: NavController,
    leaderboardViewModel: LeaderboardViewModel = viewModel()
) {
    val users by leaderboardViewModel.users.collectAsState()
    val isLoading by leaderboardViewModel.isLoading.collectAsState()

    Scaffold(
        bottomBar = {
            AppBottomNavigation(navController)
        }
    ) {innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .background(Tertiary)
                .padding(8.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Column {
                    Spacer(modifier = Modifier.height(20.dp))
                    TopBar()
                    HeadingTextComponent(value = "Leaderboard")
                    Spacer(modifier = Modifier.height(20.dp))
                    Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(start = 10.dp, end = 10.dp))
                    UserList(users = users)
                }
            }
        }
    }
}

@Composable
fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.leaderboard),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun UserList(users: List<UserLeaderboard>) {
    val topUsers = users.sortedByDescending { it.points }
    LazyColumn {
        itemsIndexed(topUsers) {index, user ->
            UserRow(index = index + 1, user = user)
            Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(start = 10.dp, end = 10.dp))  // Add Divider here
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun UserRow(index: Int, user: UserLeaderboard) {
    val painter = rememberImagePainter(
        if (user.imageUri.isEmpty())
            R.drawable.baseline_person_24
        else
            user.imageUri
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "$index.", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.width(10.dp))
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.size(50.dp).clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = "${user.firstName} ${user.lastName}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = "Score: ${user.points}", fontSize = 14.sp, color = Color.Gray)
        }
    }
}
