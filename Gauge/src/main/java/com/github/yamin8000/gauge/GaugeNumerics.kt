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

package com.github.yamin8000.gauge

/**
 * GaugeNumerics represent mathematical numbers that are used to create the [Gauge].
 * It's highly advised to choose [startAngle] (like 120) and [sweepAngle] (like 300) from highly composite/abundant numbers,
 * this way gauge is drawn more visually appealing.
 *
 * @param startAngle Gauge's starting angle, 0 represents 3 o'clock
 * @param sweepAngle size of degrees to draw the Gauge's arc and marks clockwise relative to [startAngle]
 * @param marksStep step to draw Gauge's marks on [startAngle] to [sweepAngle] range
 * @param pointsStep point's step to draw Gauge's points (bigger marks) on [startAngle] to [sweepAngle] range
 */
data class GaugeNumerics(
    val startAngle: Int,
    val sweepAngle: Int,
    val marksStep: Int = 2,
    val pointsStep: Int = 10,
)
