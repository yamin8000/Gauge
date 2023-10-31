/*
 *     Gauge/Gauge.Gauge.main
 *     Utility.kt Copyrighted by Yamin Siahmargooei at 2023/10/26
 *     Utility.kt Last modified copyright at 2023/10/26
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

package com.github.yamin8000.gauge.util

internal fun translate(
    input: Float,
    originalRange: ClosedFloatingPointRange<Float>,
    outputRange: ClosedFloatingPointRange<Float>,
): Float {
    val scale = ((outputRange.endInclusive - outputRange.start) /
            (originalRange.endInclusive - originalRange.start))
    return (input - originalRange.start) * scale
}

internal fun translate2(
    input: Float,
    originalRange: ClosedFloatingPointRange<Float>,
    outputRange: ClosedFloatingPointRange<Float>,
): Float {
    return (((input - originalRange.start) * (outputRange.endInclusive - outputRange.start)) / (originalRange.endInclusive - originalRange.start)) + outputRange.start
}