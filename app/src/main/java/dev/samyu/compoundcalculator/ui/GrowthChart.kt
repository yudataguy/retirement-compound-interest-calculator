package dev.samyu.compoundcalculator.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import dev.samyu.compoundcalculator.calculation.YearlyPoint
import dev.samyu.compoundcalculator.format.MoneyFormatter
import kotlin.math.roundToInt

@Composable
fun GrowthChart(points: List<YearlyPoint>, currencySymbol: String) {
    var selectedIndex by remember(points) { mutableIntStateOf(points.lastIndex.coerceAtLeast(0)) }
    val selected = points.getOrNull(selectedIndex)

    Column {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .pointerInput(points) {
                    detectTapGestures {
                        if (points.isEmpty() || size.width <= 0) return@detectTapGestures
                        selectedIndex = ((it.x / size.width) * points.lastIndex).roundToInt()
                            .coerceIn(0, points.lastIndex)
                    }
                }
                .pointerInput(points) {
                    detectDragGestures { change, _ ->
                        if (points.isEmpty() || size.width <= 0) return@detectDragGestures
                        selectedIndex = ((change.position.x / size.width) * points.lastIndex)
                            .roundToInt()
                            .coerceIn(0, points.lastIndex)
                    }
                }
        ) {
            if (points.isEmpty()) return@Canvas
            val maxBalance = points.maxOf { it.balance }.coerceAtLeast(1.0)
            val widthStep = if (points.size == 1) 0f else size.width / (points.size - 1)

            fun pointFor(index: Int, amount: Double): Offset {
                val x = index * widthStep
                val y = size.height - ((amount / maxBalance).toFloat() * size.height)
                return Offset(x, y)
            }

            points.zipWithNext().forEachIndexed { index, pair ->
                drawLine(
                    color = Color(0xFF9AB7A4),
                    start = pointFor(index, pair.first.contributed),
                    end = pointFor(index + 1, pair.second.contributed),
                    strokeWidth = 5f
                )
                drawLine(
                    color = Color(0xFF246B45),
                    start = pointFor(index, pair.first.balance),
                    end = pointFor(index + 1, pair.second.balance),
                    strokeWidth = 7f
                )
            }

            val marker = pointFor(selectedIndex, points[selectedIndex].balance)
            drawCircle(color = Color(0xFF17231D), radius = 9f, center = marker)
        }

        if (selected != null) {
            Text("Year ${selected.year}: ${MoneyFormatter.format(selected.balance, currencySymbol)}")
            Text("You added ${MoneyFormatter.format(selected.contributed, currencySymbol)}")
            Text("Your money earned ${MoneyFormatter.format(selected.interest, currencySymbol)}")
        }
    }
}
