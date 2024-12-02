package com.example.kpi_mobileappdev_lab6.styledcomponents

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// компонент для відображення кнопки
@Composable
fun CustomButton(
    onClickAction: () -> Unit,
    buttonContent: String,
) {
    Button (
        onClick = onClickAction,
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier.fillMaxWidth(0.8f)
    ) {
        Text(
            text = buttonContent,
            fontSize = 16.sp,
            lineHeight = 25.sp,
            modifier = Modifier.padding(vertical = 15.5.dp)
        )
    }
}
