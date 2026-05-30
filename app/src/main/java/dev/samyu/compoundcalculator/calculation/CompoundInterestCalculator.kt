package dev.samyu.compoundcalculator.calculation

import kotlin.math.max

object CompoundInterestCalculator {
    private const val COMPOUNDING_EPSILON = 0.000001

    fun calculate(input: CalculationInput): CalculationResult {
        val years = max(0, input.years)
        val totalMonths = years * 12
        val startingBalance = max(0.0, input.initialAmount)
        var contributed = startingBalance
        val annualRate = max(0.0, input.annualRatePercent) / 100.0
        val periodRate = if (annualRate == 0.0) 0.0 else annualRate / input.compoundFrequency.periodsPerYear
        val monthsPerPeriod = 12.0 / input.compoundFrequency.periodsPerYear
        val points = mutableListOf<YearlyPoint>()
        val monthlyContribution = max(0.0, input.monthlyContribution)
        val balances = mutableListOf(CompoundBalance(startingBalance))
        var balance = balances.sumOf { it.amount }

        for (month in 1..totalMonths) {
            balances.forEach { compoundBalance ->
                compoundBalance.monthsSinceCompound += 1.0
                while (compoundBalance.monthsSinceCompound + COMPOUNDING_EPSILON >= monthsPerPeriod) {
                    compoundBalance.amount += compoundBalance.amount * periodRate
                    compoundBalance.monthsSinceCompound -= monthsPerPeriod
                }
            }

            balances += CompoundBalance(monthlyContribution)
            contributed += monthlyContribution
            balance = balances.sumOf { it.amount }

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

    private data class CompoundBalance(
        var amount: Double,
        var monthsSinceCompound: Double = 0.0
    )
}
