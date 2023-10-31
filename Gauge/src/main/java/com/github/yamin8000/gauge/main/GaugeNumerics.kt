/*
 *     Gauge/Gauge.Gauge.main
 *     GaugeNumerics.kt Copyrighted by Yamin Siahmargooei at 2023/10/26
 *     GaugeNumerics.kt Last modified copyright at 2023/10/26
 *     This file is part of Gauge/Gauge.Gauge.main.
 *     Copyright (C) 2023  Yamin Siahmargooei
 *
 *     Gauge/Gauge.Gauge.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Gauge/Gauge.Gauge.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Gauge.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.yamin8000.gauge.main

/**
 * [GaugeNumerics] represent mathematical numbers that are used to visualize the [Gauge].
 *
 * It's advised that [bigTicksStep] being a multiple of [smallTicksStep] so Gauge's more visually appealing.
 *
 * @param startAngle Gauge's starting angle, 0 represents 3 o'clock
 * @param sweepAngle size of degrees to draw the Gauge's arc and ticks clockwise relative to [startAngle]
 * @param valueRange the range to bound the value
 * @param smallTicksStep step to draw Gauge's small ticks on [valueRange]
 * @param bigTicksStep step to draw Gauge's big ticks (bigger marks) on [valueRange]
 */
data class GaugeNumerics(
    val startAngle: Int,
    val sweepAngle: Int,
    val valueRange: ClosedFloatingPointRange<Float>,
    val smallTicksStep: Int = 2,
    val bigTicksStep: Int = 10,
)
