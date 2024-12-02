package com.example.kpi_mobileappdev_lab6.styledcomponents

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// компонент для відображення користувацького текстового поля
@Composable
fun CustomTextField(
    textFieldLabel: String,
    textFieldValue: String,
    updateTextFieldValue: (newTextFieldValue: String) -> Unit
) {
    OutlinedTextField(
        value = textFieldValue,
        onValueChange = { newTextFieldValue -> updateTextFieldValue(newTextFieldValue) },
        shape = RoundedCornerShape(8.dp),
        singleLine = true,
        textStyle = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        ),
        label = {
            Text(
                text = textFieldLabel,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
            )
        },
        keyboardOptions = KeyboardOptions(
            // числова клавіатура для користувацького текстового поля
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        modifier = Modifier.fillMaxWidth(0.8f)
    )
}
