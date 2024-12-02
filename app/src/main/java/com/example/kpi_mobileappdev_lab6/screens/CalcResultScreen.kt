package com.example.kpi_mobileappdev_lab6.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kpi_mobileappdev_lab6.CalcSharedViewModel
import com.example.kpi_mobileappdev_lab6.styledcomponents.BodyText
import com.example.kpi_mobileappdev_lab6.styledcomponents.CustomButton
import com.example.kpi_mobileappdev_lab6.styledcomponents.HeaderText

@Composable
fun CalcResultScreen(
    sharedViewModel: CalcSharedViewModel,
    toCalcInputScreen: () -> Unit
) {
    // результати розрахунків електричних навантажень об'єктів
    val (distributionBusResults, corporationResults) = sharedViewModel.evaluate()

    // текстові описи кожного зі значень отриманого результату (ШР1=ШР2=ШР3)
    val descriptionsDistributionBus = hashMapOf(
        "groupUtilisationRate" to "груповий коефіцієнт використання;",
        "effectiveERNumber" to "ефективна кількість ЕП;",
        "activePowerCoef" to "розрахунковий коефіцієнт активної потужності;",
        "activeLoad" to "розрахункове активне навантаження;",
        "reactiveLoad" to "розрахункове реактивне навантаження;",
        "fullPower" to "повна потужність;",
        "groupCurrent" to "розрахунковий груповий струм."
    )

    // текстові описи кожного зі значень отриманого результату (цех в цілому)
    val descriptionsCorporation = hashMapOf(
        "groupUtilisationRate" to "коефіцієнт використання;",
        "effectiveERNumber" to "ефективна кількість ЕП;",
        "activePowerCoef" to "розрахунковий коефіцієнт активної потужності;",
        "activeLoad" to "розрахункове активне навантаження на шинах 0,38 кВ ТП;",
        "reactiveLoad" to "розрахункове реактивне навантаження на шинах 0,38 кВ ТП;",
        "fullPower" to "повна потужність на шинах 0,38 кВ ТП;",
        "groupCurrent" to "розрахунковий груповий струм на шинах 0,38 кВ ТП."
    )

    // одиниці вимірювання отриманих результатів
    val resultsUnits = hashMapOf(
        "activeLoad" to "кВт",
        "reactiveLoad" to "квар.",
        "fullPower" to "кВ⋅А",
        "groupCurrent" to "А"
    )

    // головний контейнер екрану для виведення результатів розрахунку
    // електричних навантажень об'єктів
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
            .verticalScroll(rememberScrollState())
            .padding(vertical = 96.dp)
    ) {
        HeaderText("Результати", Modifier.fillMaxWidth(0.8f))

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "ШР1 = ШР2 = ШР3",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.fillMaxWidth(0.8f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        distributionBusResults.forEach { (key, value) ->
            BodyText(
                text = AnnotatedString.Builder().apply {
                    pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                    append("$value${resultsUnits[key]?.let { " $it" } ?: ""}")
                    pop()
                    append(" - ${descriptionsDistributionBus[key]}")
                }.toAnnotatedString(),
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Цех в цілому",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.fillMaxWidth(0.8f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        corporationResults.forEach { (key, value) ->
            BodyText(
                text = AnnotatedString.Builder().apply {
                    pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                    append("$value${resultsUnits[key]?.let { " $it" } ?: ""}")
                    pop()
                    append(" - ${descriptionsCorporation[key]}")
                }.toAnnotatedString(),
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        Spacer(modifier = Modifier.height(28.dp))

        CustomButton(
            onClickAction = toCalcInputScreen,
            buttonContent = "Назад",
        )
    }
}
