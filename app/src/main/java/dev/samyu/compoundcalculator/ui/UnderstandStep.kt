package dev.samyu.compoundcalculator.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.samyu.compoundcalculator.calculation.CalculationInput
import dev.samyu.compoundcalculator.calculation.CompoundInterestCalculator
import dev.samyu.compoundcalculator.format.MoneyFormatter

@Composable
fun UnderstandStep(state: AppState) {
    val input = CalculationInput(
        initialAmount = state.initialAmount.toDoubleOrNull() ?: 0.0,
        monthlyContribution = state.monthlyContribution.toDoubleOrNull() ?: 0.0,
        years = state.years.toIntOrNull() ?: 0,
        annualRatePercent = state.annualRate.toDoubleOrNull() ?: 0.0,
        compoundFrequency = state.frequency
    )
    val result = remember(input) { CompoundInterestCalculator.calculate(input) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Text("Here is what could happen")
        Text(MoneyFormatter.format(result.finalBalance, state.currencySymbol))
        Text("Ending balance")
        Spacer(Modifier.height(12.dp))
        Text("You added ${MoneyFormatter.format(result.totalContributed, state.currencySymbol)}")
        Text("Your money earned ${MoneyFormatter.format(result.totalInterest, state.currencySymbol)}")
        Spacer(Modifier.height(20.dp))
        GrowthChart(result.yearlyPoints, state.currencySymbol)
        Spacer(Modifier.height(20.dp))
        result.yearlyPoints.forEach { point ->
            Text("Year ${point.year}: ${MoneyFormatter.format(point.balance, state.currencySymbol)}")
        }
    }
}
