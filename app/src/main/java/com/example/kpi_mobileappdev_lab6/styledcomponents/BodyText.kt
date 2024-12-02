package com.example.kpi_mobileappdev_lab6.styledcomponents

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.sp

// компонент для відображення основного тексту з підтримкою форматування (AnnotatedString)
@Composable
fun BodyText(text: AnnotatedString, modifier: Modifier = Modifier) {
    Text(
        text,
        fontSize = 16.sp,
        lineHeight = 25.sp,
        modifier = modifier
    )
}
