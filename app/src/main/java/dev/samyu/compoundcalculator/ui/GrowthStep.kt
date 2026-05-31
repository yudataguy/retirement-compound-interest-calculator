package dev.samyu.compoundcalculator.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.samyu.compoundcalculator.calculation.CompoundFrequency

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GrowthStep(state: AppState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Text("Choose how it grows")
        Text("Use your best guess. This is an estimate, not a promise.")
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = state.years,
            onValueChange = { state.years = it },
            label = { Text("Years to grow") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = state.annualRate,
            onValueChange = { state.annualRate = it },
            label = { Text("Yearly interest rate (%)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        Text("How often interest is added")
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CompoundFrequency.entries.forEach { frequency ->
                FilterChip(
                    selected = state.frequency == frequency,
                    onClick = { state.frequency = frequency },
                    label = { Text(frequency.label) }
                )
            }
        }
    }
}
