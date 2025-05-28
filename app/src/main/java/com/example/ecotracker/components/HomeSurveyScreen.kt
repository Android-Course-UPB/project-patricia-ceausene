package com.example.ecotracker.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun RoundedProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.LightGray,
    progressColor: Color = MaterialTheme.colorScheme.primary,
    strokeWidth: Float = 16f,
    cornerRadius: Float = 32f
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        // Draw the background
        drawRoundRect(
            color = backgroundColor,
            size = size.copy(width = width),
            cornerRadius = CornerRadius(cornerRadius, cornerRadius)
        )

        // Draw the progress
        drawRoundRect(
            color = progressColor,
            size = size.copy(width = width * progress),
            cornerRadius = CornerRadius(cornerRadius, cornerRadius)
        )
    }
}

@Composable
fun IconWithLabel(
    iconId: Int,
    label: String,
    iconTint: Color,
    labelColor: Color = Color.Black
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        Icon(
            painter = painterResource(iconId),
            contentDescription = label,
            tint = iconTint,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            color = labelColor,
        )
    }
}