package com.example.kpi_mobileappdev_lab6

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.pow
import kotlin.math.sqrt

class CalcSharedViewModel: ViewModel() {
    // mutablestateflow для збереження параметрів електроприймачів (розподільча шина)
    private val _distributionBusParameters = MutableStateFlow(
        linkedMapOf(
            "grindingMachine" to linkedMapOf(
                "efficiencyNomVal" to "0.92",
                "loadPowerFactor" to "0.90",
                "loadVoltage" to "0.38",
                "erQuantity" to "4",
                "ratedPower" to "20.00",
                "utilisationRate" to "0.15",
                "reactivePowerFactor" to "1.33"
            ),
            "drillingMachine" to linkedMapOf(
                "efficiencyNomVal" to "0.92",
                "loadPowerFactor" to "0.90",
                "loadVoltage" to "0.38",
                "erQuantity" to "2",
                "ratedPower" to "14.00",
                "utilisationRate" to "0.12",
                "reactivePowerFactor" to "1.00"
            ),
            "jointer" to linkedMapOf(
                "efficiencyNomVal" to "0.92",
                "loadPowerFactor" to "0.90",
                "loadVoltage" to "0.38",
                "erQuantity" to "4",
                "ratedPower" to "42.00",
                "utilisationRate" to "0.15",
                "reactivePowerFactor" to "1.33"
            ),
            "circularSaw" to linkedMapOf(
                "efficiencyNomVal" to "0.92",
                "loadPowerFactor" to "0.90",
                "loadVoltage" to "0.38",
                "erQuantity" to "1",
                "ratedPower" to "36.00",
                "utilisationRate" to "0.30",
                "reactivePowerFactor" to "1.52"
            ),
            "pressMachine" to linkedMapOf(
                "efficiencyNomVal" to "0.92",
                "loadPowerFactor" to "0.90",
                "loadVoltage" to "0.38",
                "erQuantity" to "1",
                "ratedPower" to "20.00",
                "utilisationRate" to "0.50",
                "reactivePowerFactor" to "0.75"
            ),
            "polishingMachine" to linkedMapOf(
                "efficiencyNomVal" to "0.92",
                "loadPowerFactor" to "0.90",
                "loadVoltage" to "0.38",
                "erQuantity" to "1",
                "ratedPower" to "40.00",
                "utilisationRate" to "0.20",
                "reactivePowerFactor" to "1.00"
            ),
            "millingMachine" to linkedMapOf(
                "efficiencyNomVal" to "0.92",
                "loadPowerFactor" to "0.90",
                "loadVoltage" to "0.38",
                "erQuantity" to "2",
                "ratedPower" to "32.00",
                "utilisationRate" to "0.20",
                "reactivePowerFactor" to "1.00"
            ),
            "fan" to linkedMapOf(
                "efficiencyNomVal" to "0.92",
                "loadPowerFactor" to "0.90",
                "loadVoltage" to "0.38",
                "erQuantity" to "1",
                "ratedPower" to "20.00",
                "utilisationRate" to "0.65",
                "reactivePowerFactor" to "0.75"
            )
        )
    )
    // stateflow для доступу до значень параметрів електроприймачів (розподільча шина)
    // без можливості безпосередньо їх змінювати
    val distributionBusParameters = _distributionBusParameters.asStateFlow()

    // функція оновлення значень параметрів електроприймачів (розподільча шина)
    fun updateDistributionBusParameters(
        updatedParameters: LinkedHashMap<String, LinkedHashMap<String, String>>
    ) {
        _distributionBusParameters.value = updatedParameters
    }

    // навантаження цеху
    private val corporationLoad = linkedMapOf(
        "Σn" to 81.0,
        "Σ(n⋅Pн)" to 2330.0,
        "Σ(n⋅Pн⋅Kв)" to 752.0,
        "Σ(n⋅Pн⋅Kв⋅tgφ)" to 657.0,
        "Σ(n⋅P²n)" to 96399.0
    )

    // функція округлення числа до двох знаків після коми
    private fun roundNum(number: Double): String {
        return BigDecimal(number).setScale(2, RoundingMode.HALF_UP).toString()
    }

    // функція розрахунку електричних навантажень об'єктів з використанням
    // методу впорядкованих діаграм
    fun evaluate(): Pair<LinkedHashMap<String, String>, LinkedHashMap<String, String>> {
        // Σ(n⋅Pн)
        var nPnSUM = 0.0
        // Σ(n⋅Pн⋅Kв)
        var nPnKvSUM = 0.0
        // Σ(n⋅Pн⋅Kв⋅tgφ)
        var nPnSquaredSUM = 0.0

        // розрахункове реактивне навантаження розподільчої шини (Qp)
        var reactiveLoadDB = 0.0

        val numParameters: LinkedHashMap<String, LinkedHashMap<String, Double>> = LinkedHashMap()
        distributionBusParameters.value.forEach { (erKey, erValue) ->
            // приведення вхідних даних калькулятора до типу Double
            val erParameters = erValue.mapValues { (_, parameterValue) ->
                parameterValue.toDouble()
            } as LinkedHashMap<String, Double>

            // n⋅Pн для поточного електроприймача
            val nPnER = erParameters["erQuantity"]!! * erParameters["ratedPower"]!!
            erParameters["n⋅Pн"] = nPnER
            // Ip для поточного електроприймача
            erParameters["Ip"] = nPnER / sqrt(3.0) * erParameters["loadVoltage"]!! *
                    erParameters["loadPowerFactor"]!! * erParameters["efficiencyNomVal"]!!
            numParameters[erKey] = erParameters

            // n⋅Pн⋅Kв для поточного електроприймача
            val nPnKvER = nPnER * erParameters["utilisationRate"]!!
            // n⋅Pн⋅Kв⋅tgφ для поточного електроприймача
            val nPnKvtgER = nPnKvER * erParameters["reactivePowerFactor"]!!

            // обчислення необхідних для подальших розрахунків сум
            nPnSUM += nPnER
            nPnKvSUM += nPnKvER
            nPnSquaredSUM += nPnER * erParameters["ratedPower"]!!
            reactiveLoadDB += nPnKvtgER
        }

        // груповий коефіцієнт використання розподільчої шини (Kв)
        val groupUtilisationRateDB = nPnKvSUM / nPnSUM
        // ефективна кількість ЕП розподільчої шини (nₑ)
        val effectiveERNumberDB = BigDecimal(nPnSUM.pow(2) / nPnSquaredSUM)
            .setScale(0, RoundingMode.UP).toInt()
        // розрахунковий коефіцієнт активної потужності розподільчої шини (Kp) [табличне значення]
        val activePowerCoefDB = 1.25
        // розрахункове активне навантаження розподільчої шини (Pp)
        val activeLoadDB = activePowerCoefDB * nPnKvSUM
        // повна потужність розподільчої шини (Sp)
        val fullPowerDB = sqrt(activeLoadDB.pow(2) + reactiveLoadDB.pow(2))
        // розрахунковий груповий струм розподільчої шини (Ip)
        val groupCurrentDB = activeLoadDB / 0.38

        // коефіцієнт використання цеху в цілому (Kв)
        val groupUtilisationRateC = corporationLoad["Σ(n⋅Pн⋅Kв)"]!! / corporationLoad["Σ(n⋅Pн)"]!!
        // ефективна кількість ЕП цеху в цілому (nₑ)
        val effectiveERNumberC = BigDecimal(corporationLoad["Σ(n⋅Pн)"]!!.pow(2) /
                corporationLoad["Σ(n⋅P²n)"]!!).setScale(0, RoundingMode.UP).toInt()
        // розрахунковий коефіцієнт активної потужності цеху в цілому (Kp) [табличне значення]
        val activePowerCoefC = 0.7
        // розрахункове активне навантаження на шинах 0.38 кВ ТП (Pp)
        val activeLoadC = activePowerCoefC * corporationLoad["Σ(n⋅Pн⋅Kв)"]!!
        // розрахункове реактивне навантаження на шинах 0.38 кВ ТП (Qp)
        val reactiveLoadC = activePowerCoefC * corporationLoad["Σ(n⋅Pн⋅Kв⋅tgφ)"]!!
        // повна потужність на шинах 0.38 кВ ТП (Sp)
        val fullPowerC = sqrt(activeLoadC.pow(2) + reactiveLoadC.pow(2))
        // розрахунковий груповий струм на шинах 0.38 кВ ТП (Ip)
        val groupCurrentC = activeLoadC / 0.38

        return Pair(
            linkedMapOf(
                "groupUtilisationRate" to roundNum(groupUtilisationRateDB),
                "effectiveERNumber" to roundNum(effectiveERNumberDB.toDouble()),
                "activePowerCoef" to roundNum(activePowerCoefDB),
                "activeLoad" to roundNum(activeLoadDB),
                "reactiveLoad" to roundNum(reactiveLoadDB),
                "fullPower" to roundNum(fullPowerDB),
                "groupCurrent" to roundNum(groupCurrentDB)
            ),
            linkedMapOf(
                "groupUtilisationRate" to roundNum(groupUtilisationRateC),
                "effectiveERNumber" to roundNum(effectiveERNumberC.toDouble()),
                "activePowerCoef" to roundNum(activePowerCoefC),
                "activeLoad" to roundNum(activeLoadC),
                "reactiveLoad" to roundNum(reactiveLoadC),
                "fullPower" to roundNum(fullPowerC),
                "groupCurrent" to roundNum(groupCurrentC)
            )
        )
    }
}
