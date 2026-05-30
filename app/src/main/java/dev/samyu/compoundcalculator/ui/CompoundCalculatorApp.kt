package dev.samyu.compoundcalculator.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompoundCalculatorApp() {
    val state = remember { AppState() }

    AppTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Compound Calculator") }) },
            bottomBar = {
                Column(Modifier.padding(16.dp)) {
                    if (state.step < 2) {
                        Button(onClick = state::next, enabled = state.canGoNext) {
                            Text(if (state.step == 1) "See my growth" else "Next")
                        }
                    }
                    if (state.canGoBack) {
                        TextButton(onClick = state::back) { Text("Back") }
                    }
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 20.dp)
            ) {
                Text("Step ${state.step + 1} of 3")
            }
        }
    }
}
