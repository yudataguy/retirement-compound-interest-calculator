package dev.samyu.compoundcalculator.calculation

import kotlin.math.max

object CompoundInterestCalculator {
    fun calculate(input: CalculationInput): CalculationResult {
        val years = max(0, input.years)
        val totalMonths = years * 12
        val initialAmount = max(0.0, input.initialAmount)
        var contributionBalance = 0.0
        var balance = initialAmount
        var contributed = initialAmount
        val annualRate = max(0.0, input.annualRatePercent) / 100.0
        val periodRate = if (annualRate == 0.0) 0.0 else annualRate / input.compoundFrequency.periodsPerYear
        val monthsPerPeriod = 12.0 / input.compoundFrequency.periodsPerYear
        var monthsSinceCompound = 0.0
        val points = mutableListOf<YearlyPoint>()
        val monthlyContribution = max(0.0, input.monthlyContribution)

        for (month in 1..totalMonths) {
            monthsSinceCompound += 1.0
            while (monthsSinceCompound + 0.000001 >= monthsPerPeriod) {
                contributionBalance += contributionBalance * periodRate
                monthsSinceCompound -= monthsPerPeriod
            }

            contributionBalance += monthlyContribution
            contributed += monthlyContribution
            balance = initialAmount + contributionBalance

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
