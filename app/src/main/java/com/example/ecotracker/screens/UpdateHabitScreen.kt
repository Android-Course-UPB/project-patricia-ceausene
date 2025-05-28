package com.example.ecotracker.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ecotracker.components.ConfirmActionDialog
import com.example.ecotracker.data.actions.UpdateHabitViewModel
import com.example.ecotracker.data.actions.UpdateHabitViewModelFactory
import com.example.ecotracker.ui.theme.LightRed
import com.example.ecotracker.ui.theme.Primary
import com.example.ecotracker.ui.theme.Tertiary
import com.example.ecotracker.ui.theme.labelTextColor

@Composable
fun UpdateHabitScreen(
    actionId: String?,
    navController: NavController,
    viewModel: UpdateHabitViewModel = viewModel(factory = UpdateHabitViewModelFactory(actionId))
) {

    val isLoading by viewModel.isLoading.collectAsState()
    val showUpdateHabitDialog by viewModel.showUpdateHabit.collectAsState()
    val showStopHabitDialog by viewModel.showStopHabit.collectAsState()

    if (showUpdateHabitDialog) {
        ConfirmActionDialog(
            title = "Habit Update Successfully!",
            text = "Great job! You've successfully updated your habit. Keep up the good work and continue to track your progress to achieve your goals.",
            onDismiss = {
                viewModel.resetUpdateHabitDialog()
                navController.popBackStack()
            }
        )
    }

    if (showStopHabitDialog) {
        ConfirmActionDialog(
            title = "Habit Stopped Successfully!",
            text = "This habit has now stopped. From now on this will not affect your carbon footprint. Remember, every small effort counts towards reducing your carbon footprint. Feel free to explore other habits that might suit your current lifestyle better!",
            onDismiss = {
                viewModel.resetStopHabitDialog()
                navController.popBackStack()
            }
        )
    }

    if (isLoading) {
        CircularProgressIndicator()
    } else {
        UpdateHabitContent(viewModel, navController)
    }
}

@Composable
fun UpdateHabitContent(viewModel: UpdateHabitViewModel, navController: NavController) {
    val action by viewModel.action.collectAsState()
    val totalWeight by viewModel.weight.collectAsState()
    val text = buildAnnotatedString {
        append("Your Carbon Footprint is reduced by\n")

        withStyle(style = SpanStyle(color = labelTextColor)) {
            append(" ${totalWeight} CO")
        }
        withStyle(style = SpanStyle(color = labelTextColor,fontSize = 12.sp, baselineShift = BaselineShift.Subscript)) {
            append("2\n")
        }
        append("per year by continuing this habit")
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        SmallFloatingActionButton(
            onClick = {
                navController.popBackStack()
            },
            containerColor = Color.White,
            contentColor = Primary,
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(Icons.Filled.KeyboardArrowLeft, null)
        }
        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Tertiary
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Text(
                    text = action!!.title,
                    color = labelTextColor,
                    textAlign = TextAlign.Center,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text =  action!!.description,
                    color = Primary,
                    textAlign = TextAlign.Justify,
                    fontSize = 18.sp
                )
            }
        }

        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Tertiary
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = text,
                    color = Primary,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Tertiary
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Update Habit",
                    color = Primary,
                    textAlign = TextAlign.Center,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "How often will you do this action?",
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
                FrequencyHabitSlider(onButton = viewModel::updateFrequency, "Update Habit")
                Button(
                    onClick = {
                        viewModel.stopHabit()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LightRed,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Stop Habit",
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }

    }

}
