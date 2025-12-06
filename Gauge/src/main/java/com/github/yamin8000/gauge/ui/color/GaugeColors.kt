package com.github.yamin8000.gauge.ui.color

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

data class GaugeColors(
    val border: Color,
    val centerCircle: Color,
    val value: Color,
    val arc: GaugeArcColors,
    val needle: GaugeNeedleColors,
    val ticks: GaugeTicksColors
) {
    companion object {

        @Composable
        fun defaultColors() = GaugeColors(
            border = MaterialTheme.colorScheme.primaryContainer,
            centerCircle = MaterialTheme.colorScheme.tertiary,
            value = MaterialTheme.colorScheme.primary,
            arc = GaugeArcColors(
                off = MaterialTheme.colorScheme.inversePrimary,
                on = MaterialTheme.colorScheme.primary
            ),
            needle = GaugeNeedleColors(
                needle = MaterialTheme.colorScheme.primary,
                ring = MaterialTheme.colorScheme.tertiaryContainer
            ),
            ticks = GaugeTicksColors(
                smallTicks = MaterialTheme.colorScheme.inversePrimary,
                bigTicks = MaterialTheme.colorScheme.primary,
                bigTicksLabels = MaterialTheme.colorScheme.primary
            )
        )
    }
}