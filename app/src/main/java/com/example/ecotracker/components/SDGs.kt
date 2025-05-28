package com.example.ecotracker.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ecotracker.R

@Composable
fun SDGCard(number: Int) {
    when (number) {
        3 -> CardComponent(text = stringResource(id = R.string.SDG3), imageRes = R.drawable.sdg3)
        11 -> CardComponent(text = stringResource(id = R.string.SDG11), imageRes = R.drawable.sdg11)
        12 -> CardComponent(text = stringResource(id = R.string.SDG12), imageRes = R.drawable.sdg12)
        13 -> CardComponent(text = stringResource(id = R.string.SDG13), imageRes = R.drawable.sdg13)
        15 -> CardComponent(text = stringResource(id = R.string.SDG3), imageRes = R.drawable.sdg15)
    }
}

@Composable
fun CardComponent(text: String, imageRes: Int) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .background(Color.White),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .background(Color.White)
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = text,
                textAlign = TextAlign.Justify
            )
        }
    }
}

@Composable
fun SDGList(numbers: List<Int>) {
    numbers.forEach { number ->
        SDGCard(number = number)
    }
}