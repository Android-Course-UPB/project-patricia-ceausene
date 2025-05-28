package com.example.ecotracker.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ecotracker.R
import com.example.ecotracker.components.CustomSlider
import com.example.ecotracker.components.IconWithLabel
import com.example.ecotracker.components.RoundedProgressBar
import com.example.ecotracker.components.SelectableItem
import com.example.ecotracker.data.survey.SurveyViewModel
import com.example.ecotracker.model.QuestionType
import com.example.ecotracker.model.SurveyQuestion
import com.example.ecotracker.model.citiesInRomania
import com.example.ecotracker.ui.theme.Primary

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SurveyScreen(
    navController: NavController,
    surveyViewModel: SurveyViewModel = viewModel()
) {
    val currentQuestionIndex by surveyViewModel.currentQuestionIndex.collectAsState()
    val surveyQuestions = surveyViewModel.surveyQuestions
    val isLoading by surveyViewModel.isLoading.collectAsState()
    val showResult by surveyViewModel.showResult.collectAsState()
    val scrollState = rememberScrollState()

    if (isLoading) {
        CircularProgressIndicator()
    } else  if (showResult) {
        navController.navigate("calculator")
    } else {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .verticalScroll(scrollState)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconWithLabel(
                    iconId = R.drawable.baseline_home_24,
                    label = "Home",
                    iconTint = Primary
                )
                IconWithLabel(
                    iconId = R.drawable.baseline_directions_car_24,
                    label = "Transport",
                    iconTint = Primary
                )
                IconWithLabel(
                    iconId = R.drawable.baseline_airplanemode_active_24,
                    label = "Travel",
                    iconTint = Primary
                )
            }
            SurveyProgressBar(currentQuestionIndex, surveyQuestions.size)
            if (currentQuestionIndex < surveyQuestions.size) {
                SurveyQuestionScreen(
                    question = surveyQuestions[currentQuestionIndex],
                    onNext = { response -> surveyViewModel.onNext(response) },
                )
            }
        }
    }
}

@Composable
fun SurveyQuestionScreen(
    question: SurveyQuestion,
    onNext: (String) -> Unit,
) {
    var selectedAnswer by remember(question) { mutableStateOf<String?>(null) }
    var isError by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp)
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        Text(
            text = question.questionText,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp
        )
        question.info?.let {
            IconWithLabel(
                iconId = R.drawable.baseline_info_24,
                label = it,
                iconTint = Primary
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        when (question.type) {
            QuestionType.DROPDOWN -> {
                CityDropdownMenu(
                    cities = citiesInRomania,
                    selectedCity = selectedAnswer,
                    onCitySelected = { selectedAnswer = it},
                    modifier = Modifier.fillMaxWidth()
                )
                ButtonsRow(selectedAnswer = selectedAnswer, onNext = onNext)
            }
            QuestionType.RADIO -> {
                question.options.forEach { option ->
                    SelectableItem(
                        text = option,
                        isSelected = option == selectedAnswer,
                        onClick = {
                            selectedAnswer = option
                        }
                    )
                }
                ButtonsRow(selectedAnswer = selectedAnswer, onNext = onNext)
            }

            QuestionType.SLIDER -> {
                CustomSlider(
                    values = question.options,
                    onValueChange = { newValue ->
                        selectedAnswer = newValue
                    }
                )
                ButtonsRow(selectedAnswer = selectedAnswer, onNext = onNext)
            }
            QuestionType.TEXT -> {
                if (isError && selectedAnswer != null && !isNumeric(selectedAnswer!!)) {
                    Text(
                        text = "Please enter only digits",
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                OutlinedTextField(
                    value = selectedAnswer ?: "",
                    onValueChange = {
                        selectedAnswer = it
                        isError = !isNumeric(it) },
                    label = { },
                    trailingIcon = { Text(question.options[0]) },
                    modifier = Modifier.fillMaxWidth(),
                    isError = isError
                )
                ButtonsRow(selectedAnswer = selectedAnswer, onNext = onNext,  isError)
            }

        }
    }
}

fun isNumeric(input: String): Boolean {
    return input.all { it.isDigit() }
}
@Composable
fun SurveyProgressBar(currentQuestionIndex: Int, totalQuestions: Int) {
    val progress = (currentQuestionIndex + 1) / totalQuestions.toFloat()
    RoundedProgressBar(
        progress = progress,
        progressColor = Primary,
        modifier = Modifier
            .fillMaxWidth()
            .height(22.dp)
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityDropdownMenu(
    cities: List<String>,
    selectedCity: String?,
    onCitySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        TextField(
            value = selectedCity ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Select your city") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            cities.forEach { city ->
                DropdownMenuItem(onClick = {
                    onCitySelected(city)
                    expanded = false
                }, text =  {
                    Text(text = city)
                })
            }
        }
    }
}


@Composable
fun ButtonsRow(
    selectedAnswer: String?,
    onNext: (String) -> Unit,
    isError: Boolean = false
) {
    Spacer(modifier = Modifier.height(20.dp))
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = {
                selectedAnswer?.let {
                    if (!isError) {
                        onNext(it)
                } }
            }, enabled = selectedAnswer != null) {
            Text("Next")
        }
    }
}


