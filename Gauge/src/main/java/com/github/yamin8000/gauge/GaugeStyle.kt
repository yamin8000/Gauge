/*
 *     Gauge/Gauge.Gauge.main
 *     GaugeStyle.kt Copyrighted by Yamin Siahmargooei at 2023/10/26
 *     GaugeStyle.kt Last modified copyright at 2023/10/26
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

import androidx.compose.ui.graphics.StrokeCap

/**
 * Visual style of the Gauge
 *
 * @param hasOuterRing whether to show Gauge's outer ring or not
 * @param hasArcs whether to show Gauge's arcs or not
 * @param arcCap Gauge arc's [StrokeCap] type
 */
data class GaugeStyle(
    val hasOuterRing: Boolean = true,
    val hasArcs: Boolean = true,
    val handHasCircle: Boolean = true,
    val hasProgressiveArcAlpha: Boolean = true,
    val arcCap: StrokeCap = StrokeCap.Round
)
