# Compound Interest Calculator Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build a single-purpose, offline Android compound interest calculator for F-Droid with a beginner-friendly three-step wizard and an interactive graph.

**Architecture:** Use a small native Android project. Keep calculation code independent from Compose, then layer UI state, wizard screens, result components, and a custom Canvas chart on top. Avoid network access, Google services, analytics, ads, and third-party charting dependencies.

**Tech Stack:** Kotlin, Gradle Android plugin, Jetpack Compose Material 3, JUnit, Android SDK API 35.

---

## File Structure

- Create: `settings.gradle.kts` - Gradle project metadata.
- Create: `build.gradle.kts` - root plugin versions and repository policy.
- Create: `gradle.properties` - AndroidX, Kotlin, and Gradle settings.
- Create: `app/build.gradle.kts` - Android app module configuration.
- Create: `app/src/main/AndroidManifest.xml` - app manifest with no network permission.
- Create: `app/src/main/java/dev/samyu/compoundcalculator/MainActivity.kt` - Compose activity entry point.
- Create: `app/src/main/java/dev/samyu/compoundcalculator/calculation/CompoundInterestCalculator.kt` - pure Kotlin calculation engine.
- Create: `app/src/main/java/dev/samyu/compoundcalculator/calculation/CompoundModels.kt` - input, frequency, and result data models.
- Create: `app/src/main/java/dev/samyu/compoundcalculator/format/MoneyFormatter.kt` - display-only currency formatting.
- Create: `app/src/main/java/dev/samyu/compoundcalculator/ui/AppState.kt` - wizard state and input validation.
- Create: `app/src/main/java/dev/samyu/compoundcalculator/ui/CompoundCalculatorApp.kt` - top-level app shell.
- Create: `app/src/main/java/dev/samyu/compoundcalculator/ui/StartStep.kt` - Step 1 screen.
- Create: `app/src/main/java/dev/samyu/compoundcalculator/ui/GrowthStep.kt` - Step 2 screen.
- Create: `app/src/main/java/dev/samyu/compoundcalculator/ui/UnderstandStep.kt` - Step 3 screen.
- Create: `app/src/main/java/dev/samyu/compoundcalculator/ui/GrowthChart.kt` - custom Canvas graph with tap/drag selected date detail.
- Create: `app/src/main/java/dev/samyu/compoundcalculator/ui/Theme.kt` - accessible Material theme.
- Create: `app/src/test/java/dev/samyu/compoundcalculator/calculation/CompoundInterestCalculatorTest.kt` - calculation tests.
- Create: `app/src/test/java/dev/samyu/compoundcalculator/format/MoneyFormatterTest.kt` - formatter tests.
- Create: `README.md` - project purpose, privacy posture, and build instructions.
- Create: `LICENSE` - open-source license text.

## Task 1: Tooling Preflight And Android Skeleton

**Files:**
- Create: `settings.gradle.kts`
- Create: `build.gradle.kts`
- Create: `gradle.properties`
- Create: `app/build.gradle.kts`
- Create: `app/src/main/AndroidManifest.xml`

- [ ] **Step 1: Confirm Gradle can run**

Run:

```bash
gradle --version
```

Expected: Gradle prints its version. If it fails with `Failed to load native library 'libnative-platform.dylib'`, repair the local Gradle/JDK setup before creating the project. A working Gradle command is required before any Android build verification can be trusted.

- [ ] **Step 2: Create `settings.gradle.kts`**

```kotlin
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "CompoundInterestCalculator"
include(":app")
```

- [ ] **Step 3: Create `build.gradle.kts`**

```kotlin
plugins {
    id("com.android.application") version "8.7.3" apply false
    id("org.jetbrains.kotlin.android") version "1.9.25" apply false
}
```

- [ ] **Step 4: Create `gradle.properties`**

```properties
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
android.useAndroidX=true
kotlin.code.style=official
android.nonTransitiveRClass=true
```

- [ ] **Step 5: Create `app/build.gradle.kts`**

```kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "dev.samyu.compoundcalculator"
    compileSdk = 35

    defaultConfig {
        applicationId = "dev.samyu.compoundcalculator"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "0.1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2024.12.01"))
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")

    debugImplementation("androidx.compose.ui:ui-tooling")

    testImplementation("junit:junit:4.13.2")
}
```

- [ ] **Step 6: Create `app/src/main/AndroidManifest.xml`**

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
    <application
        android:allowBackup="true"
        android:label="Compound Calculator">
        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>
</manifest>
```

- [ ] **Step 7: Build skeleton**

Run:

```bash
gradle :app:assembleDebug
```

Expected: build reaches Kotlin/Android resource errors only if `MainActivity` or resources are not created yet. After Task 4, this command must pass.

- [ ] **Step 8: Commit**

```bash
git add settings.gradle.kts build.gradle.kts gradle.properties app/build.gradle.kts app/src/main/AndroidManifest.xml
git commit -m "chore: scaffold Android project"
```

## Task 2: Calculation Models And Tests

**Files:**
- Create: `app/src/main/java/dev/samyu/compoundcalculator/calculation/CompoundModels.kt`
- Create: `app/src/main/java/dev/samyu/compoundcalculator/calculation/CompoundInterestCalculator.kt`
- Create: `app/src/test/java/dev/samyu/compoundcalculator/calculation/CompoundInterestCalculatorTest.kt`

- [ ] **Step 1: Write failing tests**

```kotlin
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

        assertEquals(2_268.25, result.finalBalance, 0.01)
        assertEquals(2_200.0, result.totalContributed, 0.01)
        assertEquals(68.25, result.totalInterest, 0.01)
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
```

- [ ] **Step 2: Run tests to verify failure**

Run:

```bash
gradle :app:testDebugUnitTest --tests dev.samyu.compoundcalculator.calculation.CompoundInterestCalculatorTest
```

Expected: compile fails because `CompoundInterestCalculator`, `CalculationInput`, and `CompoundFrequency` do not exist.

- [ ] **Step 3: Create calculation models**

```kotlin
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
```

- [ ] **Step 4: Implement calculation engine**

```kotlin
package dev.samyu.compoundcalculator.calculation

import kotlin.math.max

object CompoundInterestCalculator {
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

        for (month in 1..totalMonths) {
            monthsSinceCompound += 1.0
            while (monthsSinceCompound + 0.000001 >= monthsPerPeriod) {
                balance += balance * periodRate
                monthsSinceCompound -= monthsPerPeriod
            }

            balance += max(0.0, input.monthlyContribution)
            contributed += max(0.0, input.monthlyContribution)

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
```

- [ ] **Step 5: Run tests to verify pass**

Run:

```bash
gradle :app:testDebugUnitTest --tests dev.samyu.compoundcalculator.calculation.CompoundInterestCalculatorTest
```

Expected: all three tests pass.

- [ ] **Step 6: Commit**

```bash
git add app/src/main/java/dev/samyu/compoundcalculator/calculation app/src/test/java/dev/samyu/compoundcalculator/calculation
git commit -m "feat: add compound interest engine"
```

## Task 3: Currency Formatting

**Files:**
- Create: `app/src/main/java/dev/samyu/compoundcalculator/format/MoneyFormatter.kt`
- Create: `app/src/test/java/dev/samyu/compoundcalculator/format/MoneyFormatterTest.kt`

- [ ] **Step 1: Write failing formatter tests**

```kotlin
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
}
```

- [ ] **Step 2: Run tests to verify failure**

Run:

```bash
gradle :app:testDebugUnitTest --tests dev.samyu.compoundcalculator.format.MoneyFormatterTest
```

Expected: compile fails because `MoneyFormatter` does not exist.

- [ ] **Step 3: Implement formatter**

```kotlin
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
```

- [ ] **Step 4: Run tests to verify pass**

Run:

```bash
gradle :app:testDebugUnitTest --tests dev.samyu.compoundcalculator.format.MoneyFormatterTest
```

Expected: both formatter tests pass.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/dev/samyu/compoundcalculator/format app/src/test/java/dev/samyu/compoundcalculator/format
git commit -m "feat: add display currency formatter"
```

## Task 4: Compose App Shell And Wizard State

**Files:**
- Create: `app/src/main/java/dev/samyu/compoundcalculator/MainActivity.kt`
- Create: `app/src/main/java/dev/samyu/compoundcalculator/ui/AppState.kt`
- Create: `app/src/main/java/dev/samyu/compoundcalculator/ui/Theme.kt`
- Create: `app/src/main/java/dev/samyu/compoundcalculator/ui/CompoundCalculatorApp.kt`

- [ ] **Step 1: Create state model**

```kotlin
package dev.samyu.compoundcalculator.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dev.samyu.compoundcalculator.calculation.CompoundFrequency

class AppState {
    var step by mutableIntStateOf(0)
    var initialAmount by mutableStateOf("1000")
    var monthlyContribution by mutableStateOf("100")
    var currencySymbol by mutableStateOf("$")
    var years by mutableStateOf("10")
    var annualRate by mutableStateOf("7")
    var frequency by mutableStateOf(CompoundFrequency.MONTHLY)

    val canGoBack: Boolean
        get() = step > 0

    val canGoNext: Boolean
        get() = when (step) {
            0 -> initialAmount.toDoubleOrNull() != null && monthlyContribution.toDoubleOrNull() != null
            1 -> years.toIntOrNull() != null && annualRate.toDoubleOrNull() != null
            else -> false
        }

    fun next() {
        if (canGoNext && step < 2) step += 1
    }

    fun back() {
        if (step > 0) step -= 1
    }
}
```

- [ ] **Step 2: Create theme**

```kotlin
package dev.samyu.compoundcalculator.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF246B45),
    secondary = Color(0xFF52675A),
    background = Color(0xFFF8FAF7),
    surface = Color(0xFFFFFFFF),
    onPrimary = Color.White,
    onBackground = Color(0xFF17231D),
    onSurface = Color(0xFF17231D)
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = MaterialTheme.typography,
        content = content
    )
}
```

- [ ] **Step 3: Create app shell**

```kotlin
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
```

- [ ] **Step 4: Create activity**

```kotlin
package dev.samyu.compoundcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dev.samyu.compoundcalculator.ui.CompoundCalculatorApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { CompoundCalculatorApp() }
    }
}
```

- [ ] **Step 5: Build app shell**

Run:

```bash
gradle :app:assembleDebug
```

Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 6: Commit**

```bash
git add app/src/main/java/dev/samyu/compoundcalculator/MainActivity.kt app/src/main/java/dev/samyu/compoundcalculator/ui
git commit -m "feat: add Compose wizard shell"
```

## Task 5: Wizard Input Screens

**Files:**
- Create: `app/src/main/java/dev/samyu/compoundcalculator/ui/StartStep.kt`
- Create: `app/src/main/java/dev/samyu/compoundcalculator/ui/GrowthStep.kt`
- Modify: `app/src/main/java/dev/samyu/compoundcalculator/ui/CompoundCalculatorApp.kt`

- [ ] **Step 1: Create Step 1 screen**

```kotlin
package dev.samyu.compoundcalculator.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun StartStep(state: AppState) {
    Column {
        Text("Start with what you know")
        Text("Enter the money you have now and what you plan to add each month.")
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = state.initialAmount,
            onValueChange = { state.initialAmount = it },
            label = { Text("Money you have now") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = state.monthlyContribution,
            onValueChange = { state.monthlyContribution = it },
            label = { Text("Money you add each month") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
        Spacer(Modifier.height(12.dp))
        Text("Currency symbol")
        listOf("$", "€", "£", "¥").forEach { symbol ->
            FilterChip(
                selected = state.currencySymbol == symbol,
                onClick = { state.currencySymbol = symbol },
                label = { Text(symbol) }
            )
        }
    }
}
```

- [ ] **Step 2: Create Step 2 screen**

```kotlin
package dev.samyu.compoundcalculator.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.samyu.compoundcalculator.calculation.CompoundFrequency

@Composable
fun GrowthStep(state: AppState) {
    Column {
        Text("Choose how it grows")
        Text("Use your best guess. This is an estimate, not a promise.")
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = state.years,
            onValueChange = { state.years = it },
            label = { Text("Years") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = state.annualRate,
            onValueChange = { state.annualRate = it },
            label = { Text("Yearly interest rate (%)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
        Spacer(Modifier.height(12.dp))
        Text("How often interest is added")
        CompoundFrequency.entries.forEach { frequency ->
            FilterChip(
                selected = state.frequency == frequency,
                onClick = { state.frequency = frequency },
                label = { Text(frequency.label) }
            )
        }
    }
}
```

- [ ] **Step 3: Wire screens into app shell**

Replace the temporary `Text("Step ${state.step + 1} of 3")` area with:

```kotlin
when (state.step) {
    0 -> StartStep(state)
    1 -> GrowthStep(state)
    else -> UnderstandStep(state)
}
```

- [ ] **Step 4: Build**

Run:

```bash
gradle :app:assembleDebug
```

Expected: compile fails because `UnderstandStep` is not created yet. Task 6 resolves it.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/dev/samyu/compoundcalculator/ui/StartStep.kt app/src/main/java/dev/samyu/compoundcalculator/ui/GrowthStep.kt app/src/main/java/dev/samyu/compoundcalculator/ui/CompoundCalculatorApp.kt
git commit -m "feat: add wizard input steps"
```

## Task 6: Results Screen And Interactive Chart

**Files:**
- Create: `app/src/main/java/dev/samyu/compoundcalculator/ui/UnderstandStep.kt`
- Create: `app/src/main/java/dev/samyu/compoundcalculator/ui/GrowthChart.kt`

- [ ] **Step 1: Create chart component**

```kotlin
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
                    fun updateSelection(x: Float) {
                        if (points.isEmpty()) return
                        val index = ((x / size.width) * (points.lastIndex)).roundToInt()
                            .coerceIn(0, points.lastIndex)
                        selectedIndex = index
                    }

                    detectTapGestures { updateSelection(it.x) }
                }
                .pointerInput(points) {
                    detectDragGestures { change, _ -> updateSelection(change.position.x) }
                }
        ) {
            if (points.isEmpty()) return@Canvas
            val maxBalance = points.maxOf { it.balance }.coerceAtLeast(1.0)
            val widthStep = if (points.size == 1) size.width else size.width / (points.size - 1)
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
```

- [ ] **Step 2: Create result screen**

```kotlin
package dev.samyu.compoundcalculator.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.samyu.compoundcalculator.calculation.CalculationInput
import dev.samyu.compoundcalculator.calculation.CompoundInterestCalculator
import dev.samyu.compoundcalculator.format.MoneyFormatter

@Composable
fun UnderstandStep(state: AppState) {
    val input = CalculationInput(
        initialAmount = state.initialAmount.toDoubleOrNull() ?: 0.0,
        monthlyContribution = state.monthlyContribution.toDoubleOrNull() ?: 0.0,
        years = state.years.toIntOrNull() ?: 0,
        annualRatePercent = state.annualRate.toDoubleOrNull() ?: 0.0,
        compoundFrequency = state.frequency
    )
    val result = remember(input) { CompoundInterestCalculator.calculate(input) }

    Column {
        Text("Here is what could happen")
        Text(MoneyFormatter.format(result.finalBalance, state.currencySymbol))
        Text("Ending balance")
        Spacer(Modifier.height(12.dp))
        Text("You added ${MoneyFormatter.format(result.totalContributed, state.currencySymbol)}")
        Text("Your money earned ${MoneyFormatter.format(result.totalInterest, state.currencySymbol)}")
        Spacer(Modifier.height(20.dp))
        GrowthChart(result.yearlyPoints, state.currencySymbol)
        Spacer(Modifier.height(20.dp))
        result.yearlyPoints.forEach { point ->
            Text("Year ${point.year}: ${MoneyFormatter.format(point.balance, state.currencySymbol)}")
        }
    }
}
```

- [ ] **Step 3: Build**

Run:

```bash
gradle :app:assembleDebug
```

Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/dev/samyu/compoundcalculator/ui/UnderstandStep.kt app/src/main/java/dev/samyu/compoundcalculator/ui/GrowthChart.kt
git commit -m "feat: add results screen and chart"
```

## Task 7: Polish Beginner UX And F-Droid Documentation

**Files:**
- Modify: `app/src/main/java/dev/samyu/compoundcalculator/ui/StartStep.kt`
- Modify: `app/src/main/java/dev/samyu/compoundcalculator/ui/GrowthStep.kt`
- Modify: `app/src/main/java/dev/samyu/compoundcalculator/ui/UnderstandStep.kt`
- Create: `README.md`
- Create: `LICENSE`

- [ ] **Step 1: Improve beginner labels**

Ensure these exact user-facing labels are present:

```text
Money you have now
Money you add each month
Years to grow
Yearly interest rate (%)
How often interest is added
Ending balance
You added
Your money earned
```

- [ ] **Step 2: Create `README.md`**

```markdown
# Compound Interest Calculator

An offline Android compound interest calculator designed for F-Droid.

The app helps people estimate how money can grow from:

- a starting amount
- a monthly contribution
- a time period
- an estimated yearly interest rate
- a compounding frequency

## Privacy

This app does not use ads, analytics, tracking, Google services, or network access.

## Distribution

The intended distribution channel is F-Droid. The app is not designed for Google Play publishing.

## Build

```bash
gradle :app:assembleDebug
```

## Test

```bash
gradle :app:testDebugUnitTest
```
```

- [ ] **Step 3: Create `LICENSE`**

Use the MIT License with the copyright holder:

```text
MIT License

Copyright (c) 2026 Sam Yu

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

- [ ] **Step 4: Verify no network permission**

Run:

```bash
grep -R "android.permission.INTERNET" -n app/src/main AndroidManifest.xml
```

Expected: no matches.

- [ ] **Step 5: Run final tests and build**

Run:

```bash
gradle :app:testDebugUnitTest
gradle :app:assembleDebug
```

Expected: both commands end with `BUILD SUCCESSFUL`.

- [ ] **Step 6: Commit**

```bash
git add app README.md LICENSE
git commit -m "docs: document F-Droid app posture"
```

## Self-Review

- Spec coverage: Task 1 covers the native Android/F-Droid skeleton. Tasks 2 and 3 cover offline calculation and currency display. Tasks 4 and 5 cover the three-step wizard. Task 6 covers result summary, year-by-year breakdown, graph, and tap/drag date context. Task 7 covers beginner wording, README, license, and no network permission.
- Red-flag scan: no unresolved implementation gaps are intentionally left in this plan.
- Type consistency: `CalculationInput`, `CompoundFrequency`, `CalculationResult`, `YearlyPoint`, `AppState`, `MoneyFormatter`, `GrowthChart`, and `UnderstandStep` are named consistently across tasks.
