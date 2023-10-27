/*
 *     Gauge/Gauge.Gauge.main
 *     Gauge.kt Copyrighted by Yamin Siahmargooei at 2023/10/24
 *     Gauge.kt Last modified copyright at 2023/10/24
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

import androidx.annotation.FloatRange
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

/**
 * Gauge Composable is a fusion of classic and modern Gauges with some customization options.
 *
 * @param value current value of the gauge, this value effects Gauge's arc and Gauge's indicator (hand)
 * @param valueRange the range to bound the value
 * @param totalSize total size of this Gauge as a Composable
 * @param numerics refer to [GaugeNumerics]
 * @param style refer to [GaugeStyle]
 * @param colors refer to [GaugeColors]
 *
 * @throws IllegalArgumentException when some parameters are inconsistent with the design
 */
@Composable
fun Gauge(
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier,
    totalSize: Dp = LocalConfiguration.current.screenWidthDp.dp,
    numerics: GaugeNumerics,
    style: GaugeStyle = GaugeStyle(),
    colors: GaugeColors = GaugeColors(
        outerRing = MaterialTheme.colorScheme.primaryContainer,
        innerRing = MaterialTheme.colorScheme.tertiaryContainer,
        centerCircle = MaterialTheme.colorScheme.tertiary,
        offArc = MaterialTheme.colorScheme.inversePrimary,
        onArc = MaterialTheme.colorScheme.primary,
        marks = MaterialTheme.colorScheme.inversePrimary,
        markPoints = MaterialTheme.colorScheme.primary,
        hand = MaterialTheme.colorScheme.primary
    )
) {
    require(value in valueRange) { "Gauge value: $value is out of Gauge Value range $valueRange" }
    require(numerics.sweepAngle < 360) { "Sweep angle: ${numerics.sweepAngle} cannot be bigger than 360 degrees, Sweep angles bigger than 360 degrees draws wrong arcs." }

    BoxWithConstraints(
        modifier = modifier.fillMaxWidth(),
        content = {
            val size = if (totalSize > maxWidth) maxWidth
            else if (totalSize > maxHeight) maxHeight
            else totalSize
            val totalAngle = numerics.startAngle + numerics.sweepAngle
            val marksSizeFraction = remember { .8f }
            Canvas(
                modifier = Modifier
                    .width(size)
                    .height(size),
                onDraw = {
                    if (style.hasOuterRing) {
                        drawRing(
                            diameter = size,
                            color = colors.outerRing,
                            ringFraction = .05f,
                            offset = center
                        )
                    }
                    drawMarks(
                        numerics = numerics,
                        totalAngle = totalAngle,
                        colors = colors,
                        size = size
                    )
                    if (style.hasArcs) {
                        drawArcs(
                            size = size,
                            style = style,
                            marksSizeFraction = marksSizeFraction,
                            onArcColor = colors.onArc,
                            offArcColor = colors.offArc,
                            numerics = numerics,
                            value = value,
                            valueRange = valueRange,
                            totalAngle = totalAngle,
                            hasProgressiveAlpha = style.hasProgressiveArcAlpha
                        )
                    }
                    drawRing(
                        diameter = size / 10,
                        color = colors.innerRing,
                        ringFraction = .3f,
                        offset = center
                    )
                    val valueDegrees =
                        translate2(
                            value,
                            valueRange,
                            numerics.startAngle.toFloat()..totalAngle.toFloat()
                        )
                    val radian = Math.toRadians(valueDegrees.toDouble())
                    val cos = cos(radian).toFloat()
                    val sin = sin(radian).toFloat()
                    val x = translate(cos, -1f..1f, 0f..size.toPx())
                    val y = translate(sin, -1f..1f, 0f..size.toPx())
                    drawLine(
                        color = colors.hand,
                        start = center,
                        strokeWidth = 10f,
                        cap = StrokeCap.Round,
                        end = Offset(
                            x.minus(cos.times(size.toPx() / 10f)),
                            y.minus(sin.times(size.toPx() / 10f))
                        )
                    )
                    if (style.handHasCircle) {
                        drawCircle(
                            color = colors.offArc,
                            radius = size.toPx() / 50,
                            center = Offset(
                                x.minus(cos.times(size.toPx() / 10f)),
                                y.minus(sin.times(size.toPx() / 10f))
                            )
                        )
                    }
                    drawCircle(
                        color = colors.centerCircle,
                        radius = size.toPx() / 50,
                        center = center
                    )
                }
            )
        }
    )
}

private fun DrawScope.drawMarks(
    numerics: GaugeNumerics,
    totalAngle: Int,
    colors: GaugeColors,
    size: Dp
) {
    val startRatio = size.toPx().div(9f)
    for (degree in numerics.startAngle..totalAngle step numerics.marksStep) {
        val isPoint = degree % numerics.pointsStep == 0
        val endRatio = if (isPoint) size.toPx().div(4f) else size.toPx().div(4.5f)
        val width = if (isPoint) 2f else 1f
        val markPointColor = if (isPoint) colors.markPoints else colors.marks

        val radian = Math.toRadians(degree.toDouble())
        val cos = cos(radian).toFloat()
        val sin = sin(radian).toFloat()
        val x = translate(cos, -1f..1f, 0f..size.toPx())
        val y = translate(sin, -1f..1f, 0f..size.toPx())
        drawLine(
            color = markPointColor,
            strokeWidth = width,
            cap = StrokeCap.Butt,
            start = Offset(
                x.minus(cos.times(startRatio)),
                y.minus(sin.times(startRatio))
            ),
            end = Offset(
                x.minus(cos.times(endRatio)),
                y.minus(sin.times(endRatio))
            )
        )
    }
}

private fun DrawScope.drawArcs(
    size: Dp,
    style: GaugeStyle,
    marksSizeFraction: Float,
    onArcColor: Color,
    offArcColor: Color,
    numerics: GaugeNumerics,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    totalAngle: Int,
    hasProgressiveAlpha: Boolean
) {
    val arcStroke = Stroke(
        width = size.toPx() / 15f,
        miter = 0f,
        cap = style.arcCap
    )
    val arcSize = Size(
        size.times(marksSizeFraction).toPx(),
        size.times(marksSizeFraction).toPx()
    )
    val arcTopLeft = Offset(
        size.times((1 - marksSizeFraction) / 2).toPx(),
        size.times((1 - marksSizeFraction) / 2).toPx()
    )
    drawArc(
        color = offArcColor,
        startAngle = numerics.startAngle.toFloat(),
        sweepAngle = numerics.sweepAngle.toFloat(),
        useCenter = false,
        style = arcStroke,
        size = arcSize,
        topLeft = arcTopLeft
    )
    drawArc(
        color = onArcColor,
        alpha = if (hasProgressiveAlpha) value / valueRange.endInclusive else 1f,
        startAngle = numerics.startAngle.toFloat(),
        sweepAngle = translate(
            value,
            valueRange.start..valueRange.endInclusive,
            numerics.startAngle.toFloat()..totalAngle.toFloat()
        ),
        useCenter = false,
        style = arcStroke,
        size = arcSize,
        topLeft = arcTopLeft
    )
}

internal fun DrawScope.drawRing(
    color: Color,
    diameter: Dp = 100.dp,
    @FloatRange(from = 0.0, 1.0)
    ringFraction: Float = .1f,
    offset: Offset = Offset.Zero
) {
    val path = Path().apply {
        val size = diameter.toPx()
        addOval(Rect(0f, 0f, size, size))
        op(
            path1 = this,
            path2 = Path().apply {
                addOval(
                    Rect(0f, 0f, size * (1 - ringFraction), size * (1 - ringFraction))
                )
                translate(Offset(size * ringFraction / 2, size * ringFraction / 2))
            },
            operation = PathOperation.Difference
        )
        if (offset != Offset.Zero)
            translate(offset.copy(offset.x - size / 2, offset.y - size / 2))
    }
    drawPath(path, color)
}