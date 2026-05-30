package dev.samyu.compoundcalculator.format

import org.junit.Assert.assertEquals
import org.junit.Test

class MoneyFormatterTest {
    @Test
    fun formatsUsdWithoutExchangeLogic() {
        assertEquals("$1,234.57", MoneyFormatter.format(1234.567, "$"))
    }

    @Test
    fun formatsSelectedSymbol() {
        assertEquals("€90.00", MoneyFormatter.format(90.0, "€"))
    }

    @Test
    fun fallsBackToUsdSymbolWhenBlank() {
        assertEquals("$12.00", MoneyFormatter.format(12.0, ""))
    }
}
