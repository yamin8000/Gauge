/*
 *     Gauge/Gauge.Gauge.main
 *     GaugeArcStyle.kt Copyrighted by Yamin Siahmargooei at 2023/10/31
 *     GaugeArcStyle.kt Last modified copyright at 2023/10/31
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

package com.github.yamin8000.gauge.ui.style

import androidx.compose.ui.graphics.StrokeCap

/**
 * Visual style of Gauge's arc
 *
 * @param hasArcs whether to show Gauge's arcs or not
 * @param hasProgressiveAlpha whether to progressively increase arc's alpha according to gauge value
 * @param bigTicksHasLabels whether to show numbers/labels for big ticks
 * @param cap Gauge arc's [StrokeCap] type
 */
data class GaugeArcStyle(
    val hasArcs: Boolean = true,
    val hasProgressiveAlpha: Boolean = true,
    val bigTicksHasLabels: Boolean = true,
    val cap: StrokeCap = StrokeCap.Round,
)