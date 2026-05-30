package dev.samyu.compoundcalculator.calculation

import org.junit.Assert.assertEquals
import org.junit.Test

class CompoundInterestCalculatorTest {
    @Test
    fun zeroInterestReturnsContributionsOnly() {
        val result = CompoundInterestCalculator.calculate(
            CalculationInput(
                initialAmount = 1_000.0,
                monthlyContribution = 100.0,
                years = 2,
                annualRatePercent = 0.0,
                compoundFrequency = CompoundFrequency.MONTHLY
            )
        )

        assertEquals(3_400.0, result.finalBalance, 0.01)
        assertEquals(3_400.0, result.totalContributed, 0.01)
        assertEquals(0.0, result.totalInterest, 0.01)
        assertEquals(2, result.yearlyPoints.size)
    }

    @Test
    fun monthlyCompoundingWithMonthlyContributionCalculatesGrowth() {
        val result = CompoundInterestCalculator.calculate(
            CalculationInput(
                initialAmount = 1_000.0,
                monthlyContribution = 100.0,
                years = 1,
                annualRatePercent = 12.0,
                compoundFrequency = CompoundFrequency.MONTHLY
            )
        )

        assertEquals(2_395.08, result.finalBalance, 0.01)
        assertEquals(2_200.0, result.totalContributed, 0.01)
        assertEquals(195.08, result.totalInterest, 0.01)
    }

    @Test
    fun annualCompoundingAppliesToFullAccountBalanceAtYearEnd() {
        val result = CompoundInterestCalculator.calculate(
            CalculationInput(
                initialAmount = 1_000.0,
                monthlyContribution = 100.0,
                years = 1,
                annualRatePercent = 12.0,
                compoundFrequency = CompoundFrequency.ANNUALLY
            )
        )

        assertEquals(2_452.0, result.finalBalance, 0.01)
        assertEquals(2_200.0, result.totalContributed, 0.01)
        assertEquals(252.0, result.totalInterest, 0.01)
    }

    @Test
    fun quarterlyCompoundingAppliesToFullAccountBalanceEachQuarter() {
        val result = CompoundInterestCalculator.calculate(
            CalculationInput(
                initialAmount = 1_000.0,
                monthlyContribution = 100.0,
                years = 1,
                annualRatePercent = 12.0,
                compoundFrequency = CompoundFrequency.QUARTERLY
            )
        )

        assertEquals(2_405.70, result.finalBalance, 0.01)
        assertEquals(2_200.0, result.totalContributed, 0.01)
        assertEquals(205.70, result.totalInterest, 0.01)
    }

    @Test
    fun dailyCompoundingUsesAccountLevelFractionalMonthlyIntervals() {
        val result = CompoundInterestCalculator.calculate(
            CalculationInput(
                initialAmount = 1_000.0,
                monthlyContribution = 100.0,
                years = 1,
                annualRatePercent = 12.0,
                compoundFrequency = CompoundFrequency.DAILY
            )
        )

        assertEquals(2_396.26, result.finalBalance, 0.01)
        assertEquals(2_200.0, result.totalContributed, 0.01)
        assertEquals(196.26, result.totalInterest, 0.01)
    }

    @Test
    fun negativeInputsClampToZero() {
        val result = CompoundInterestCalculator.calculate(
            CalculationInput(
                initialAmount = -500.0,
                monthlyContribution = -100.0,
                years = -2,
                annualRatePercent = -10.0,
                compoundFrequency = CompoundFrequency.MONTHLY
            )
        )

        assertEquals(0.0, result.finalBalance, 0.01)
        assertEquals(0.0, result.totalContributed, 0.01)
        assertEquals(0.0, result.totalInterest, 0.01)
        assertEquals(0, result.yearlyPoints.size)
    }

    @Test
    fun zeroYearsReturnsInitialAmountOnly() {
        val result = CompoundInterestCalculator.calculate(
            CalculationInput(
                initialAmount = 500.0,
                monthlyContribution = 200.0,
                years = 0,
                annualRatePercent = 8.0,
                compoundFrequency = CompoundFrequency.DAILY
            )
        )

        assertEquals(500.0, result.finalBalance, 0.01)
        assertEquals(500.0, result.totalContributed, 0.01)
        assertEquals(0.0, result.totalInterest, 0.01)
        assertEquals(0, result.yearlyPoints.size)
    }
}
