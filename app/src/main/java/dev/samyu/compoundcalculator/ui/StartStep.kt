package dev.samyu.compoundcalculator.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun StartStep(state: AppState) {
    Column {
        Text("Start with what you know")
        Text("Enter the money you have now and what you plan to add each month.")
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = state.initialAmount,
            onValueChange = { state.initialAmount = it },
            label = { Text("Money you have now") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = state.monthlyContribution,
            onValueChange = { state.monthlyContribution = it },
            label = { Text("Money you add each month") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
        Spacer(Modifier.height(12.dp))
        Text("Currency symbol")
        listOf("$", "€", "£", "¥").forEach { symbol ->
            FilterChip(
                selected = state.currencySymbol == symbol,
                onClick = { state.currencySymbol = symbol },
                label = { Text(symbol) }
            )
        }
    }
}
