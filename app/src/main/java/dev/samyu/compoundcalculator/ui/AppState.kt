package dev.samyu.compoundcalculator.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dev.samyu.compoundcalculator.calculation.CompoundFrequency

class AppState {
    var step by mutableIntStateOf(0)
    var initialAmount by mutableStateOf("1000")
    var monthlyContribution by mutableStateOf("100")
    var currencySymbol by mutableStateOf("\$")
    var years by mutableStateOf("10")
    var annualRate by mutableStateOf("7")
    var frequency by mutableStateOf(CompoundFrequency.MONTHLY)

    val canGoBack: Boolean
        get() = step > 0

    val canGoNext: Boolean
        get() = when (step) {
            0 -> initialAmount.toDoubleOrNull() != null && monthlyContribution.toDoubleOrNull() != null
            1 -> years.toIntOrNull() != null && annualRate.toDoubleOrNull() != null
            else -> false
        }

    fun next() {
        if (canGoNext && step < 2) step += 1
    }

    fun back() {
        if (step > 0) step -= 1
    }
}
