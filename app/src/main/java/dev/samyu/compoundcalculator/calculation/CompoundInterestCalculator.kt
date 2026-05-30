package dev.samyu.compoundcalculator.calculation

import kotlin.math.max

object CompoundInterestCalculator {
    private const val COMPOUNDING_EPSILON = 0.000001

    fun calculate(input: CalculationInput): CalculationResult {
        val years = max(0, input.years)
        val totalMonths = years * 12
        var balance = max(0.0, input.initialAmount)
        var contributed = balance
        val annualRate = max(0.0, input.annualRatePercent) / 100.0
        val periodRate = if (annualRate == 0.0) 0.0 else annualRate / input.compoundFrequency.periodsPerYear
        val monthsPerPeriod = 12.0 / input.compoundFrequency.periodsPerYear
        var monthsSinceCompound = 0.0
        val points = mutableListOf<YearlyPoint>()
        val monthlyContribution = max(0.0, input.monthlyContribution)

        for (month in 1..totalMonths) {
            monthsSinceCompound += 1.0
            while (monthsSinceCompound + COMPOUNDING_EPSILON >= monthsPerPeriod) {
                balance += balance * periodRate
                monthsSinceCompound -= monthsPerPeriod
            }

            balance += monthlyContribution
            contributed += monthlyContribution

            if (month % 12 == 0) {
                points += YearlyPoint(
                    year = month / 12,
                    month = month,
                    balance = balance,
                    contributed = contributed,
                    interest = balance - contributed
                )
            }
        }

        return CalculationResult(
            finalBalance = balance,
            totalContributed = contributed,
            totalInterest = balance - contributed,
            yearlyPoints = points
        )
    }
}
