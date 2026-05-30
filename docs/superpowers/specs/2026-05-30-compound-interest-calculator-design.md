# Compound Interest Calculator Design

## Goal

Build a single-purpose, open-source Android compound interest calculator for F-Droid. The app should help users with limited financial literacy understand how a starting amount, monthly contributions, time, interest rate, and compounding frequency affect long-term growth.

The first release is intentionally focused: no accounts, saved scenarios, sync, ads, analytics, Google services, network access, taxes, inflation, or exchange rates.

## Platform And Distribution

- Native Android app built with Kotlin and Jetpack Compose.
- Distributed outside Google Play, with F-Droid as the intended channel.
- No network permission.
- No proprietary Google services.
- No advertising, analytics, tracking, or crash reporting SDK.
- Dependencies should remain minimal and open-source friendly.
- Include an open-source license and README in the implementation phase.

## User Experience

The app uses a three-step wizard because that is easier for beginner users than a dense financial form.

### Step 1: Start

Collect the basic money inputs:

- Initial amount.
- Monthly contribution.
- Currency symbol.

Currency selection only changes display formatting. The app does not perform exchange-rate conversion.

### Step 2: Growth

Collect the growth assumptions:

- Length of time in years.
- Estimated annual interest rate.
- Compound frequency:
  - annually
  - semiannually
  - quarterly
  - monthly
  - daily

The language should avoid jargon where possible. Field helper text should explain concepts in plain language, following the spirit of public calculators such as Investor.gov.

### Step 3: Understand

Show the calculated projection in a beginner-friendly result view:

- Ending balance.
- Total money added.
- Interest earned.
- A graph showing balance over time.
- A visual distinction between money the user contributed and growth from interest.
- A simple year-by-year breakdown table.

Labels should use direct language such as "You added" and "Your money earned" rather than finance-heavy terminology.

## Calculation Model

The calculator should use deterministic offline math. The first version assumes monthly contributions happen at the end of each month.

The calculation should produce:

- final balance
- total initial amount plus contributions
- total interest earned
- yearly data points for graphing and the table

The implementation should simulate the projection over time rather than relying only on a closed-form formula. This keeps recurring contributions and different compounding frequencies easier to reason about and test.

## Graph

Use a custom Compose Canvas chart instead of a third-party charting library. This keeps the app lightweight for F-Droid and avoids adding a dependency for one focused visual.

The graph should be readable on small screens:

- balance line
- contributed-money line or shaded reference
- simple axis labels
- no dense grid or finance-dashboard styling

## Architecture

Keep the app small but separated into clear units:

- calculation model and engine
- UI state model
- wizard screens
- result summary
- chart component
- formatting helpers

The calculation engine should be independent from Compose so it can be unit tested.

## Testing

Add unit tests for the calculation engine:

- zero interest
- no monthly contribution
- monthly contributions with monthly compounding
- different compounding frequencies
- invalid or edge inputs such as zero years

Add UI-level smoke verification where practical after the Android project is scaffolded.

## Non-Goals For Version 1

- Saved scenarios.
- Scenario comparison.
- Inflation adjustment.
- Tax adjustment.
- Contribution timing controls.
- Contribution increases over time.
- Account import/export.
- Exchange-rate conversion.
- Publishing to Google Play.
