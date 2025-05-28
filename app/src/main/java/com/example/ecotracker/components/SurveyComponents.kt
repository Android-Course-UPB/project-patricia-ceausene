package com.example.ecotracker.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.ecotracker.ui.theme.Primary
import kotlin.math.roundToInt

@Composable
fun CustomSlider(
    values: List<String>,
    onValueChange: (String) -> Unit
) {
    var sliderPosition by remember {mutableFloatStateOf(0f) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(16.dp)
    ) {
        Slider(
            value = sliderPosition,
            onValueChange = {
                sliderPosition = it
                onValueChange(values[sliderPosition.roundToInt()])
            },
            valueRange = 0f..(values.size - 1).toFloat(),
            steps = values.size - 2,
            modifier = Modifier
                .fillMaxWidth()
        )
        Text(
            text = buildAnnotatedString {
                append("Selected: ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(values[sliderPosition.roundToInt()])
                }
            },
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Composable
fun SelectableItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() }
            .background(color = if (isSelected) Primary else Color.White)
            .border(width = 2.dp, color = Primary)
            .padding(vertical = 16.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else Primary
        )
    }
}

//@Composable
//fun TransportOptionRow(
//    option: String,
//    selectedValue: String?,
//    onSelectedChange: (String) -> Unit
//) {
//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween,
//        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
//    ) {
//        Text(option)
//        Row(
//            horizontalArrangement = Arrangement.SpaceEvenly,
//            modifier = Modifier.weight(1f)
//        ) {
//            listOf("0 h", "0 - 5 h", "5 - 10 h", "10 - 15 h").forEach { hourRange ->
//                RadioButton(
//                    selected = selectedValue == hourRange,
//                    onClick = { onSelectedChange(hourRange) }
//                )
//                Text(text = hourRange, modifier = Modifier.padding(start = 4.dp))
//            }
//        }
//    }
//}

//@Composable
//fun TransportQuestion() {
//    val transportOptions = listOf("Bus", "Minibus", "Subway", "Tram")
//    val selectedValues = remember { mutableStateMapOf<String, String?>() }
//
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Top,
//        modifier = Modifier.fillMaxSize().padding(16.dp)
//    ) {
//        transportOptions.forEach { option ->
//            TransportOptionRow(
//                option = option,
//                selectedValue = selectedValues[option],
//                onSelectedChange = { selectedValues[option] = it }
//            )
//        }
//    }
//}