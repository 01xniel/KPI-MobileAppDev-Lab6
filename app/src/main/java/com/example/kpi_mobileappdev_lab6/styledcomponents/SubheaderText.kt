package com.example.kpi_mobileappdev_lab6.styledcomponents

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// компонент для відображення підзаголовків
@Composable
fun SubheaderText(text: String, modifier: Modifier) {
    Text(
        text,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        modifier = modifier
    )
}
