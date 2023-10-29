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
 * @param hasBorder whether to show Gauge's border or not
 * @param borderWidth width of the Gauge's outer border
 * @param hasArcs whether to show Gauge's arcs or not
 * @param needleTipHasCircle whether to show a circle in tip of the needle
 * @param needleHasRing whether to show a border/ring in the start of the needle on Gauge's center
 * @param needleRingWidth width of the needle's border
 * @param hasProgressiveArcAlpha whether to progressively increase arc's alpha according to gauge value
 * @param bigTicksHasLabels whether to show numbers/labels for big ticks
 * @param arcCap Gauge arc's [StrokeCap] type
 */
data class GaugeStyle(
    val hasBorder: Boolean = true,
    val borderWidth: Float = 20f,
    val hasArcs: Boolean = true,
    val needleTipHasCircle: Boolean = true,
    val needleHasRing: Boolean = true,
    val needleRingWidth: Float = 20f,
    val hasProgressiveArcAlpha: Boolean = true,
    val bigTicksHasLabels: Boolean = true,
    val arcCap: StrokeCap = StrokeCap.Round
)
