package com.example.kpi_mobileappdev_lab6.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.LastPage
import androidx.compose.material.icons.filled.FirstPage
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kpi_mobileappdev_lab6.CalcSharedViewModel
import com.example.kpi_mobileappdev_lab6.styledcomponents.HeaderText
import com.example.kpi_mobileappdev_lab6.styledcomponents.CustomButton
import com.example.kpi_mobileappdev_lab6.styledcomponents.CustomTextField
import com.example.kpi_mobileappdev_lab6.styledcomponents.SubheaderText
import kotlinx.coroutines.launch

// функція перевірки коректності введеного значення в текстовому полі
fun isNewValueValid(value: String): Boolean {
    return if (value.isEmpty()) {
        true
    } else if (value.all { it.isDigit() || it == '.' }) {
        value.count { it == '.' } < 2
    } else {
        false
    }
}

@Composable
fun CalcInputScreen(
    sharedViewModel: CalcSharedViewModel,
    toCalcResultScreen: () -> Unit
) {
    // отримання параметрів розподільчої шини з sharedViewModel
    val dbParameters by sharedViewModel.distributionBusParameters.collectAsStateWithLifecycle()

    // поточний контекст застосунку
    val context = LocalContext.current

    // кількість сторінок (одна сторінка на кожен електроприймач)
    val numberOfPages = dbParameters.size
    // стан пейджера
    val pagerState = rememberPagerState(pageCount = { numberOfPages })
    // корутинний контекст
    val coroutineScope = rememberCoroutineScope()

    // назви електроприймачів
    val erLabels = hashMapOf(
        "grindingMachine" to "Шліфувальний верстат",
        "drillingMachine" to "Свердлильний верстат",
        "jointer" to "Фугувальний верстат",
        "circularSaw" to "Циркулярна пила",
        "pressMachine" to "Прес",
        "polishingMachine" to "Полірувальний верстат",
        "millingMachine" to "Фрезерний верстат",
        "fan" to "Вентилятор"
    )

    // назви параметрів
    val erParametersLabels = hashMapOf(
        "efficiencyNomVal" to "Номінальне значення ККД",
        "loadPowerFactor" to "Коефіцієнт потужності навантаження",
        "loadVoltage" to "Напруга навантаження",
        "erQuantity" to "Кількість ЕП",
        "ratedPower" to "Номінальна потужність ЕП",
        "utilisationRate" to "Коефіцієнт використання",
        "reactivePowerFactor" to "Коефіцієнт реактивної потужності"
    )

    // головний контейнер екрану для введення параметрів калькулятора
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
            .verticalScroll(rememberScrollState())
            .padding(vertical = 96.dp)
    ) {
        HeaderText("Вхідні дані", Modifier.fillMaxWidth(0.8f))

        Spacer(modifier = Modifier.height(32.dp))

        // горизонтальний пейджер для відображення параметрів кожного з електроприймачів
        HorizontalPager(
            state = pagerState,
        ) { erIndex ->
            val erKey = dbParameters.keys.elementAt(erIndex)
            val erValue = dbParameters[erKey]!!

            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                erValue.forEach { (erParameterKey, erParameterValue) ->
                    CustomTextField(
                        textFieldLabel = erParametersLabels[erParameterKey]!!,
                        textFieldValue = erParameterValue,
                        updateTextFieldValue = { newValue ->
                            if (isNewValueValid(newValue)) {
                                val newERParameters = LinkedHashMap(dbParameters[erKey])
                                newERParameters[erParameterKey] = newValue
                                val newDBParameters = LinkedHashMap(dbParameters)
                                newDBParameters[erKey] = newERParameters
                                sharedViewModel.updateDistributionBusParameters(newDBParameters)
                            }
                        }
                    )
                }
                SubheaderText(
                    text = erLabels[erKey]!!,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))
        
        // панель навігації між сторінками
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // кнопка "перша сторінка"
            Icon(
                imageVector = Icons.Default.FirstPage,
                contentDescription = null,
                modifier = Modifier.clickable(
                    // вилучення анімації при натисканні на кнопку
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    coroutineScope.launch { pagerState.animateScrollToPage(0) }
                }
            )
            Spacer(modifier = Modifier.width(16.dp))
            // кнопка "попередня сторінка"
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = null,
                modifier = Modifier.clickable(
                    // вилучення анімації при натисканні на кнопку
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    coroutineScope.launch {
                        val previousPage = (pagerState.currentPage - 1).coerceAtLeast(0)
                        pagerState.animateScrollToPage(previousPage)
                    }
                }
            )
            Spacer(modifier = Modifier.width(16.dp))
            // індикатор поточної сторінки
            repeat(numberOfPages) { pageIndex ->
                val isCurrentPage = pagerState.currentPage == pageIndex
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(if (isCurrentPage) Color.DarkGray else Color.LightGray)
                        .size(8.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            // кнопка "наступна сторінка"
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                modifier = Modifier.clickable(
                    // вилучення анімації при натисканні на кнопку
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    coroutineScope.launch {
                        val nextPage = (pagerState.currentPage + 1).coerceAtMost(numberOfPages - 1)
                        pagerState.animateScrollToPage(nextPage)
                    }
                }
            )
            Spacer(modifier = Modifier.width(16.dp))
            // кнопка "остання сторінка"
            Icon(
                imageVector = Icons.AutoMirrored.Filled.LastPage,
                contentDescription = null,
                modifier = Modifier.clickable(
                    // вилучення анімації при натисканні на кнопку
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(numberOfPages - 1)
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        CustomButton(
            onClickAction = {
                val areFieldsNotEmpty = dbParameters.all { (_, erValue) ->
                    erValue.all { (_, parameterValue) -> parameterValue.isNotEmpty() }
                }
                if (areFieldsNotEmpty) {
                    // якщо всі параметри заповнені, то здійснюються перехід
                    // до сторінки з результатами
                    toCalcResultScreen()
                } else {
                    // якщо хоча б один параметр порожній, то виводиться
                    // відповідне повідомлення
                    Toast.makeText(
                        context,
                        "Усі параметри мають бути заповнені",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            buttonContent = "Розрахувати"
        )
    }
}
