package com.example.ecotracker.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ecotracker.R
import com.example.ecotracker.components.ConfirmActionDialog
import com.example.ecotracker.components.IconWithText
import com.example.ecotracker.components.RoundedProgressBar
import com.example.ecotracker.components.SDGList
import com.example.ecotracker.components.TextLabel
import com.example.ecotracker.data.actions.ActionDetailViewModelFactory
import com.example.ecotracker.data.actions.ActionDetailsViewModel
import com.example.ecotracker.model.Action
import com.example.ecotracker.ui.theme.Primary
import com.example.ecotracker.ui.theme.Secondary
import com.example.ecotracker.ui.theme.Tertiary
import com.example.ecotracker.ui.theme.labelColor
import com.example.ecotracker.ui.theme.labelTextColor
import kotlin.math.roundToInt
import kotlin.reflect.KFunction1

@Composable
fun DetailActionScreen(
    navController: NavController,
    actionId: String?,
    viewModel: ActionDetailsViewModel = viewModel(factory = ActionDetailViewModelFactory(actionId))
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val showCommitHabitDialog by viewModel.showCommitHabitAlertDialog.collectAsState()
    val showDoneActionDialog by viewModel.showDoneActionAlertDialog.collectAsState()

    if (showDoneActionDialog) {
        ConfirmActionDialog(
            title = "Action tracked!",
            text = "You've done a great thing for the planet and earned some points in the process.",
            onDismiss = viewModel::resetActionDialog
        )
    }
    if (showCommitHabitDialog) {
        ConfirmActionDialog(
            title = "Congrats!",
            text = "You have turned this action into an ongoing habit! Don't forget to update your habits if your behaviour changes.",
            onDismiss = viewModel::resetHabitDialog
        )
    }
    if (isLoading) {
        CircularProgressIndicator()
    } else {
        ActionDetailContent(viewModel, navController)
    }
}


@Composable
fun ActionDetailContent(viewModel: ActionDetailsViewModel, navController: NavController) {

    val action by viewModel.action.collectAsState()
    val presses by viewModel.presses.collectAsState()
    val showHabitDialog by viewModel.showHabitDialog.collectAsState()
    val isHabit by viewModel.isHabit.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Tertiary)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SmallFloatingActionButton(
            onClick = {
                viewModel.submitChanges()
                navController.popBackStack()
                      },
            containerColor = Color.White,
            contentColor = Primary,
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(Icons.Filled.KeyboardArrowLeft, null)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (action!!.saveMoney) {
                TextLabel(
                    text = "Save Money",
                    textColor = labelTextColor,
                    surfaceColor = labelColor,
                )
            }
            if (action!!.food) {
                TextLabel(
                    text = "Food",
                    textColor = labelTextColor,
                    surfaceColor = labelColor
                )
            }
            if (action!!.travel) {
                TextLabel(
                    text = "Travel",
                    textColor = labelTextColor,
                    surfaceColor = labelColor
                )
            }
            if (action!!.stayHealthy) {
                TextLabel(
                    text = "Stay Healthy",
                    textColor = labelTextColor,
                    surfaceColor = labelColor
                )
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
        DetailActionCard(actionItem = action!!)
        Spacer(modifier = Modifier.height(4.dp))
        Button(
            onClick = {
                viewModel.incrementPresses()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Secondary,
                contentColor = Primary,
                disabledContainerColor = Color.Transparent
            ),
            enabled = !isHabit
        ) {
            Text(
                text = "I did it!",
                color = Primary,
                textAlign = TextAlign.Center,
                fontSize = 18.sp
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        TrackHabitComponent(presses = presses, isHabit)
        Spacer(modifier = Modifier.height(2.dp))
        if (presses >= 6 && !showHabitDialog && !isHabit) Button(
            onClick = {
                viewModel.turnIntoHabit()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Secondary,
                contentColor = Primary
            )
        ) {
            Text(
                text = "Turn it into a habit",
                color = Primary,
                textAlign = TextAlign.Center,
                fontSize = 18.sp
            )
        }
        if (showHabitDialog && !isHabit) {
            FrequencyHabitSlider(viewModel::commitToHabit,  "I commit to this habit!")
        } else if (!isHabit){
            RoundedProgressBar(
                progress = minOf(presses / 6f, 1.0f),
                progressColor = Primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .height(12.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Sustainable Development Goals",
            color = Primary
        )
        Text(
            text = "By completing this action you are directly contributing to the following United Nations Sustainable Development Goals",
            color = Primary,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(20.dp)
        )
        SDGList(numbers = action!!.sdgs)
        Spacer(modifier = Modifier.height(4.dp))
    }
}


@Composable
fun DetailActionCard(
    actionItem: Action
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
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
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = actionItem.description,
                fontSize = 18.sp,
                textAlign = TextAlign.Justify
            )
            Spacer(modifier = Modifier.height(4.dp))
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
fun TrackHabitComponent(presses: Int, isHabit: Boolean) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(8.dp)
    ) {
        val textWithAnnotation = remember(presses, isHabit) {
            if(isHabit) {
                buildAnnotatedString {
                    append("You've already formed a habit of this action, nice one!")
                }
            } else if (presses < 6) {
                val remainingPresses = 6 - presses
                buildAnnotatedString {
                    append("Track this ")
                    // Inline content to make part of the text bold
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("$remainingPresses more times")
                    }
                    append(" to form a habit.")
                }
            } else {
                buildAnnotatedString {
                    append("You have tracked this action ")
                    // Inline content to make part of the text bold
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("$presses ")
                    }
                    append("times.")
                }
            }
        }
        Text(
            text = textWithAnnotation,
            fontSize = 18.sp
        )
    }
}

@Composable
fun FrequencyHabitSlider(
    onButton: KFunction1<String, Unit>,
    buttonText: String
) {
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    val options = listOf(
        "Once a month", "Twice a month", "Three times a month",
        "Once a week", "Twice a week", "Three times a week",
        "Four times a week","Five times a week","Six times a week",
        "Everyday"
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Slider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            valueRange = 0f..(options.size - 1).toFloat(),
            steps = options.size - 2,  // Total options minus two because range includes both endpoints
            colors = SliderDefaults.colors(
                thumbColor = Primary,     // Color of the circle (thumb)
                activeTrackColor = Color.White,   // Color of the active part of the track
                inactiveTrackColor = Color.White,  // Color of the inactive part of the track
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .height(12.dp)
        )
        Text(
            text = buildAnnotatedString {
                append("Selected: ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(options[sliderPosition.roundToInt()])
                }
            },
            color = Primary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        Button(
            onClick = {
               onButton.invoke(options[sliderPosition.roundToInt()])
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Secondary,
                contentColor = Primary
            )
        ) {
            Text(
                text = buttonText,
                color = Primary,
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )
        }
    }
}

