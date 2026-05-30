package dev.samyu.compoundcalculator.format

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object MoneyFormatter {
    private val format = DecimalFormat("#,##0.00", DecimalFormatSymbols(Locale.US))

    fun format(amount: Double, symbol: String): String {
        val safeSymbol = symbol.ifBlank { "$" }
        return safeSymbol + format.format(amount)
    }
}
