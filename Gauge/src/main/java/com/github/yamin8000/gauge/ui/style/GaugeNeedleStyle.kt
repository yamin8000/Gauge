/*
 *     Gauge/Gauge.Gauge.main
 *     GaugeNeedleStyle.kt Copyrighted by Yamin Siahmargooei at 2023/10/31
 *     GaugeNeedleStyle.kt Last modified copyright at 2023/10/31
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

/**
 * Visual style of Gauge's needle
 *
 * @param hasNeedle whether to show Gauge's needle or not
 * @param tipHasCircle whether to show a circle in tip of the needle
 * @param hasRing whether to show a border/ring in the start of the needle on Gauge's center
 * @param ringWidth width of the needle's border
 */
data class GaugeNeedleStyle(
    val hasNeedle: Boolean = true,
    val tipHasCircle: Boolean = true,
    val hasRing: Boolean = true,
    val ringWidth: Float = 20f,
)
