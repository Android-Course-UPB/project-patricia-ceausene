package com.example.ecotracker.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ecotracker.data.home.WeeklyTip

@Composable
fun WeeklyTipComponent(
    weeklyTip: WeeklyTip?
) {
    Box {
        Column {
            HeadingTextComponent(value = weeklyTip?.title ?: "Loading...")
            TipTextComponent(value = weeklyTip?.introduction ?: "")
            Spacer(modifier = Modifier.height(8.dp))
            weeklyTip?.tip1?.let {
                TipTextComponent(value = "1. $it")
            }
            Spacer(modifier = Modifier.height(8.dp))
            weeklyTip?.tip2?.let {
                TipTextComponent(value = "2. $it")
            }
            Spacer(modifier = Modifier.height(8.dp))
            TipTextComponent(value = weeklyTip?.ending ?: "")
        }
    }
}
