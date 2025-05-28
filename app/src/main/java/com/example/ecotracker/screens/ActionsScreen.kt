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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ecotracker.R
import com.example.ecotracker.components.AppBottomNavigation
import com.example.ecotracker.components.FilterChipComponent
import com.example.ecotracker.components.GoToTop
import com.example.ecotracker.components.IconWithText
import com.example.ecotracker.components.TitleTextComponent
import com.example.ecotracker.data.actions.ActionsViewModel
import com.example.ecotracker.model.Action
import com.example.ecotracker.ui.theme.Tertiary
import kotlinx.coroutines.launch


@Composable
fun ActionsListScreenWithFirestore(
    navController: NavController,
    actionsViewModel: ActionsViewModel = viewModel()
) {

    when (val result = actionsViewModel.response.value) {
        is ActionsViewModel.ActionState.Loading -> {
            // Display a loading indicator
            Log.d("ActionsScreen", "Loading actions")
            CircularProgressIndicator()
        }
        is ActionsViewModel.ActionState.Success -> {
            ActionsScreen(navController, actions = result.actions, actionsViewModel)
        }
        is ActionsViewModel.ActionState.Error -> {
            // Display an error message
            Log.d("ActionsScreen", "Error while loading actions")
            Text("Error")
        }
        else -> {
            Log.d("ActionsScreen", "empty")
            Text("empty")
        }
    }
}

@Composable
fun ActionsScreen(
    navController: NavController,
    actions: List<Action>,
    actionsViewModel: ActionsViewModel
) {
    var selectedStayHealthy by remember {
        mutableStateOf(false)
    }
    var selectedSaveMoney by remember {
        mutableStateOf(false)
    }
    var selectedTravel by remember {
        mutableStateOf(false)
    }
    var selectedFoodAndDrink by remember {
        mutableStateOf(false)
    }
    val filteredActions = remember(selectedStayHealthy, selectedSaveMoney, selectedTravel, selectedFoodAndDrink) {
        actions.filter {
                    (!selectedStayHealthy || it.stayHealthy) &&
                    (!selectedSaveMoney || it.saveMoney) &&
                    (!selectedTravel || it.travel) &&
                    (!selectedFoodAndDrink || it.food)
        }
    }
    val listState = rememberLazyListState()
    val showButton by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0
        }
    }

    Scaffold(
        bottomBar = {
            AppBottomNavigation(navController)
        },
        floatingActionButton = {
            if (showButton) {
                GoToTop {
                    actionsViewModel.viewModelScope.launch {
                        listState.scrollToItem(0)
                    }
                }
            }
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .background(Tertiary)
                .padding(8.dp)
                .fillMaxSize()
        ) {
            Column {
                TitleTextComponent(value = "All Actions")
                Row(
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    FilterChipComponent(
                        selectedValue = selectedStayHealthy,
                        text = "Stay Healthy",
                        onSelectedChanged = { selectedStayHealthy = it}
                    )
                    FilterChipComponent(
                        selectedValue = selectedFoodAndDrink,
                        text = "Food",
                        onSelectedChanged = { selectedFoodAndDrink = it}
                    )
                    FilterChipComponent(
                        selectedValue = selectedSaveMoney,
                        text = "Save Money",
                        onSelectedChanged = { selectedSaveMoney = it}
                    )
                    FilterChipComponent(
                        selectedValue = selectedTravel,
                        text = "Travel",
                        onSelectedChanged = { selectedTravel = it}
                    )
                }
                LazyColumn(
                    state = listState
                ) {
                    items(filteredActions) { actionItem ->
                        ActionCard(navController = navController, actionItem = actionItem)
                    }
                }
            }
        }
    }
}


@Composable
fun ActionCard(
    navController: NavController,
    actionItem: Action
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
            Text(
                text = actionItem.title,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = actionItem.description,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
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