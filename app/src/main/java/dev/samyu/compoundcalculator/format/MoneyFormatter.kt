package dev.samyu.compoundcalculator.format

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object MoneyFormatter {
    fun format(amount: Double, symbol: String): String {
        val format = DecimalFormat("#,##0.00", DecimalFormatSymbols(Locale.US))
        val safeSymbol = symbol.ifBlank { "$" }
        return safeSymbol + format.format(amount)
    }
}
