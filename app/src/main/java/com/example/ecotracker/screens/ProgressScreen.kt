package com.example.ecotracker.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.ecotracker.R
import com.example.ecotracker.components.AppBottomNavigation
import com.example.ecotracker.components.CircleWithDynamicText
import com.example.ecotracker.components.IconWithText
import com.example.ecotracker.components.RoundedProgressBar
import com.example.ecotracker.components.TitleTextComponent
import com.example.ecotracker.data.progress.ProgressViewModel
import com.example.ecotracker.model.ProgressAction
import com.example.ecotracker.ui.theme.Primary
import com.example.ecotracker.ui.theme.Tertiary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressScreen(
    navController: NavController,
    progressViewModel: ProgressViewModel = viewModel()
) {

    val progressActions by progressViewModel.actions.collectAsState()
    val progressHabits by progressViewModel.habits.collectAsState()
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }
    val tabs = listOf("My actions", "My habits")

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    LaunchedEffect(navBackStackEntry) {
        progressViewModel.loadData()
    }

    Scaffold(
        bottomBar = {
            AppBottomNavigation(navController)
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .background(Tertiary)
                .padding(8.dp)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                TitleTextComponent(value = "Your actions and habits")
                Spacer(modifier = Modifier.height(4.dp))
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = Color.Transparent
                        )
                    },
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = index == selectedTabIndex,
                            onClick = { selectedTabIndex = index },
                            text = { Text(title) },
                            selectedContentColor = Color.White,
                            unselectedContentColor = Primary,
                            modifier = Modifier
                                .background(
                                    color = if (index == selectedTabIndex) Primary else Color.White,
                                    shape = RoundedCornerShape(30.dp)
                                )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                when (selectedTabIndex) {
                    0 -> LazyColumn {
                        items(progressActions) { progressAction ->
                            ProgressActionCard(
                                navController = navController,
                                actionItem = progressAction
                            )
                        }
                    }
                    1 -> LazyColumn {
                        items(progressHabits) { progressHabit ->
                            ProgressHabitCard(
                                navController = navController,
                                actionItem = progressHabit
                            )
                        }
                    }
                }

            }
        }
    }

}

@Composable
fun ProgressActionCard(
    navController: NavController,
    actionItem: ProgressAction
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                navController.navigate("detailAction/${actionItem.id}")
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = actionItem.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f)
                )
                CircleWithDynamicText(number = actionItem.counter)
            }
            Spacer(modifier = Modifier.height(4.dp))
            if (actionItem.counter >= 6) {
                Text(
                    text = "You can now turn this action into a habit!"
                )
            } else {
                Text(
                    text = "Track this ${6-actionItem.counter} more times to form a habit."
                )
                RoundedProgressBar(
                    progress = minOf(actionItem.counter / 6f, 1.0f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 10.dp)
                        .height(12.dp),
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconWithText(
                        text = actionItem.weight,
                        painterResource = painterResource(R.drawable.baseline_arrow_downward_24)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    IconWithText(
                        text = "+${actionItem.points}",
                        painterResource = painterResource(R.drawable.star)
                    )
                }
            }
        }
    }
}

@Composable
fun ProgressHabitCard(
    navController: NavController,
    actionItem: ProgressAction
    ) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                Log.d("Action id", actionItem.id)
                navController.navigate("updateHabit/${actionItem.id}")
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(
                text = actionItem.title,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconWithText(
                        text = actionItem.weight,
                        painterResource = painterResource(R.drawable.baseline_arrow_downward_24)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    IconWithText(
                        text = "+${actionItem.points}",
                        painterResource = painterResource(R.drawable.star)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Icon(
                    painter = painterResource(R.drawable.baseline_access_time_24),
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = actionItem.frequency,
                    fontSize = 18.sp
                )
            }
        }
    }
}
