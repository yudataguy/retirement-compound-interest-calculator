package dev.samyu.compoundcalculator.calculation

enum class CompoundFrequency(val periodsPerYear: Int, val label: String) {
    ANNUALLY(1, "Annually"),
    SEMIANNUALLY(2, "Semiannually"),
    QUARTERLY(4, "Quarterly"),
    MONTHLY(12, "Monthly"),
    DAILY(365, "Daily")
}

data class CalculationInput(
    val initialAmount: Double,
    val monthlyContribution: Double,
    val years: Int,
    val annualRatePercent: Double,
    val compoundFrequency: CompoundFrequency
)

data class YearlyPoint(
    val year: Int,
    val month: Int,
    val balance: Double,
    val contributed: Double,
    val interest: Double
)

data class CalculationResult(
    val finalBalance: Double,
    val totalContributed: Double,
    val totalInterest: Double,
    val yearlyPoints: List<YearlyPoint>
)
