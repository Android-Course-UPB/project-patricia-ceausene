package com.example.ecotracker.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecotracker.R
import com.example.ecotracker.ui.theme.Neutral
import com.example.ecotracker.ui.theme.Primary
import com.example.ecotracker.ui.theme.Secondary
import com.example.ecotracker.ui.theme.Shapes
import com.example.ecotracker.ui.theme.TextColor
import com.example.ecotracker.ui.theme.labelColor
import com.example.ecotracker.ui.theme.labelIconColor
import com.example.ecotracker.ui.theme.labelTextColor
import com.example.ecotracker.ui.theme.transparentGray


@Composable
fun NormalTextComponent(value:String) {
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal
        ),
        color = colorResource(id = R.color.colorBlack),
        textAlign = TextAlign.Center
    )
}

@Composable
fun TipTextComponent(value:String) {
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth(),
        style = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal
        ),
        color = colorResource(id = R.color.colorBlack),
        textAlign = TextAlign.Justify
    )
}

@Composable
fun UnderlinedTextComponent(value:String) {
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal
        ),
        color = colorResource(id = R.color.colorGrey),
        textAlign = TextAlign.Center,
        textDecoration = TextDecoration.Underline
    )
}
@Composable
fun HeadingTextComponent(value:String) {
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal
        ),
        color = colorResource(id = R.color.colorBlack),
        textAlign = TextAlign.Center
    )
}

@Composable
fun TitleTextComponent(value:String) {
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal
        ),
        color = Primary,
        textAlign = TextAlign.Left
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTextField(label: String, painterResource: Painter, onTextSelected: (String) -> Unit, errorStatus: Boolean = false) {
    var textValue by remember {
        mutableStateOf("")
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(Shapes.small),
        label = { Text(label) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Primary,
            focusedLabelColor = Primary,
            cursorColor = Primary,
            containerColor = Neutral
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        singleLine = true,
        maxLines = 1,
        value = textValue,
        onValueChange = {
            textValue = it
            onTextSelected(it)
        },
        leadingIcon = {
            Icon(painter = painterResource, contentDescription = null)
        },
        isError = errorStatus
    )
}
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PasswordTextField(label: String, painterResource: Painter, onTextSelected: (String) -> Unit, errorStatus: Boolean = false) {
        var password by remember {
            mutableStateOf("")
        }
        var passwordVisible by remember {
            mutableStateOf(false)
        }

        val localFocusManager = LocalFocusManager.current

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .clip(Shapes.small),
            label = { Text(label) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Primary,
                focusedLabelColor = Primary,
                cursorColor = Primary,
                containerColor = Neutral
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions {
                localFocusManager.clearFocus()
            },
            singleLine = true,
            maxLines = 1,
            value = password,
            onValueChange = {
                password = it
                onTextSelected(it)
            },
            leadingIcon = {
                Icon(painter = painterResource, contentDescription = null)
            },
            trailingIcon = {
                val iconImage = if (passwordVisible) {
                    painterResource(R.drawable.visibility)
                } else {
                    painterResource(R.drawable.visibility_off)
                }

                val description = if (passwordVisible) {
                    stringResource(R.string.hide_password)
                } else {
                    stringResource(R.string.show_password)
                }
                IconButton(onClick = { passwordVisible = !passwordVisible}) {
                    Icon(painter = iconImage, contentDescription = description)
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            isError = errorStatus
        )
    }



@Composable
fun ButtonComponent(value: String, onButtonClicked: () -> Unit) {
    Button(
        onClick = {
            onButtonClicked.invoke()
        },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp),
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(Color.Transparent)
        ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(48.dp)
                .background(
                    brush = Brush.horizontalGradient(listOf(Secondary, Primary)),
                    shape = RoundedCornerShape(50.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun DividerTextComponent() {
    Row(modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically) {
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = Color.Gray,
            thickness = 1.dp
        )
        Text(
            modifier = Modifier.padding(8.dp),
            text = stringResource(R.string.or),
            fontSize = 18.sp,
            color = TextColor
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = Color.Gray,
            thickness = 1.dp
        )
    }
}

@Composable
fun ClickableLoginTextComponent(tryingToLogin: Boolean = true, onTextSelected: (String) -> Unit) {
    val initialText = if (tryingToLogin) "Already have an account? " else "Don't have an account yet? "
    val loginText = if (tryingToLogin) "Login" else "Register"

    val annotatedString = buildAnnotatedString {
        append(initialText)
        withStyle(style = SpanStyle(color = Primary)) {
            pushStringAnnotation(tag = loginText, annotation = loginText)
            append(loginText)
        }
    }
    
    ClickableText(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 21.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Center
        ),
        text = annotatedString,
        onClick = { offset ->
            annotatedString.getStringAnnotations(offset, offset)
                .firstOrNull()?.also { span ->
                    Log.d("ClickableTextComponent", "{${span.item}}")

                    if (span.item == loginText) {
                        onTextSelected(span.item)
                    }
                }
        }
    )
}

@Composable
fun IconWithText(text: String, painterResource: Painter, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(labelColor, shape = RoundedCornerShape(16.dp))
            .padding(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(labelIconColor, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource,
                    contentDescription = null, // Set appropriate content description if needed
                    tint = labelTextColor // Adjust the tint color of the icon
                )
            }
            Spacer(modifier = Modifier.width(8.dp)) // Add spacing between icon and text
            Text(
                text = text,
                fontWeight = FontWeight.Normal,
                fontStyle = FontStyle.Normal,
                fontSize = 14.sp,
                color = labelTextColor
            )
        }
    }
}


@Composable
fun WeightAndPointsComponent(weight: String, points: Number) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconWithText(
                text = weight,
                painterResource = painterResource(R.drawable.baseline_arrow_downward_24)
            )
            Spacer(modifier = Modifier.width(10.dp))
            IconWithText(
                text = "+${points}",
                painterResource = painterResource(R.drawable.star)
            )
        }
    }
}

@Composable
fun TextLabel(text:String, textColor: Color, surfaceColor: Color) {
    Box(modifier = Modifier
        .background(surfaceColor, shape = RoundedCornerShape(16.dp))
        .padding(8.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            textAlign = TextAlign.Center,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterChipComponent(
    selectedValue: Boolean,
    text: String,
    onSelectedChanged: (Boolean) -> Unit
) {
    FilterChip(
        onClick = {onSelectedChanged(!selectedValue) },
        label = {
            Text(
                text = text,
                maxLines = 1,
                fontSize = 12.sp
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Color.White,
            labelColor = Primary,
            selectedContainerColor = Primary,
            selectedLabelColor = Color.White,
        ),
        shape = RoundedCornerShape(20.dp),
        border = FilterChipDefaults.filterChipBorder(
            borderColor = Color.White
        ),
        selected = selectedValue,
    )
}

@Composable
fun GoToTop(goToTop: () -> Unit) {
    FloatingActionButton(
        modifier = Modifier
            .padding(16.dp)
            .size(50.dp),
        onClick = goToTop,
        containerColor = transparentGray,
        contentColor = Color.Black,
    ) {
        Icon(
            imageVector = Icons.Filled.KeyboardArrowUp,
            contentDescription = "go to top"
        )
    }
}

@Composable
fun CircleWithDynamicText(number: Int) {
    // Box is used to create a layered layout where the Text widget can sit in the center of the circle
    Box(
        contentAlignment = Alignment.Center,  // Center the Text inside the Box
        modifier = Modifier
            .size(26.dp)  // Set the size of the circle
            .background(color = labelColor, shape = CircleShape)  // Blue circle
    ) {
        Text(
            text = "${number}x",
            color = labelTextColor,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun ConfirmActionDialog(
    title: String,
    text: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(title) },
        text = { Text(text) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}