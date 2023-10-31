/*
 *     Gauge/Gauge.Gauge.main
 *     GaugeStyle.kt Copyrighted by Yamin Siahmargooei at 2023/10/31
 *     GaugeStyle.kt Last modified copyright at 2023/10/31
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
 * Visual style of the Gauge
 *
 * @param hasBorder whether to show Gauge's border or not
 * @param hasValueText whether to show Gauge's value as a text
 * @param borderWidth width of the Gauge's outer border
 * @param arcStyle visual style of Gauge's arc
 * @param needleStyle visual style of Gauge's needle
 */
data class GaugeStyle(
    val hasBorder: Boolean = true,
    val hasValueText: Boolean = true,
    val borderWidth: Float = 20f,
    val arcStyle: GaugeArcStyle = GaugeArcStyle(),
    val needleStyle: GaugeNeedleStyle = GaugeNeedleStyle()
)
