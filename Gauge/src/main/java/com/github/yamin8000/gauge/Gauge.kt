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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

/**
 * Gauge Composable is a fusion of classic and modern Gauges with some customization options.
 *
 * @param value current value of the gauge, this value effects Gauge's arc and Gauge's indicator (hand)
 * @param modifier
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
    modifier: Modifier = Modifier,
    totalSize: Dp = LocalConfiguration.current.screenWidthDp.dp,
    numerics: GaugeNumerics,
    style: GaugeStyle = GaugeStyle(
        borderWidth = totalSize.value / 20f,
        needleRingWidth = totalSize.value / 30f
    ),
    colors: GaugeColors = GaugeColors(
        border = MaterialTheme.colorScheme.primaryContainer,
        needleRing = MaterialTheme.colorScheme.tertiaryContainer,
        centerCircle = MaterialTheme.colorScheme.tertiary,
        offArc = MaterialTheme.colorScheme.inversePrimary,
        onArc = MaterialTheme.colorScheme.primary,
        smallTicks = MaterialTheme.colorScheme.inversePrimary,
        needle = MaterialTheme.colorScheme.primary,
        bigTicks = MaterialTheme.colorScheme.primary,
        bigTicksLabels = MaterialTheme.colorScheme.tertiary
    )
) {
    require(value in numerics.valueRange) { "Gauge value: $value is out of Gauge Value range ${numerics.valueRange}" }
    require(numerics.sweepAngle in 1..360) { "Sweep angle: ${numerics.sweepAngle} must be from 1 to 360" }

    BoxWithConstraints(
        modifier = modifier.fillMaxWidth(),
        content = {
            val textMeasurer = rememberTextMeasurer()
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
                    if (style.hasBorder) {
                        drawCircle(
                            color = colors.border,
                            center = center,
                            style = Stroke(style.borderWidth)
                        )
                    }
                    drawMarks(
                        numerics = numerics,
                        totalAngle = totalAngle,
                        colors = colors,
                        size = size,
                        textMeasurer = textMeasurer,
                        valueRange = numerics.valueRange,
                        hasNumbers = style.bigTicksHasLabels
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
                            valueRange = numerics.valueRange,
                            totalAngle = totalAngle,
                            hasProgressiveAlpha = style.hasProgressiveArcAlpha
                        )
                    }
                    if (style.needleHasRing) {
                        drawCircle(
                            color = colors.needleRing,
                            center = center,
                            style = Stroke(style.needleRingWidth),
                            radius = size.toPx() / 25
                        )
                    }
                    val valueDegrees = translate2(
                        value,
                        numerics.valueRange,
                        numerics.startAngle.toFloat()..totalAngle.toFloat()
                    )
                    val radian = Math.toRadians(valueDegrees.toDouble())
                    val cos = cos(radian).toFloat()
                    val sin = sin(radian).toFloat()
                    val x = translate(cos, -1f..1f, 0f..size.toPx())
                    val y = translate(sin, -1f..1f, 0f..size.toPx())
                    drawLine(
                        color = colors.needle,
                        start = center,
                        strokeWidth = 10f,
                        cap = StrokeCap.Round,
                        end = Offset(
                            x.minus(cos.times(size.toPx() / 10f)),
                            y.minus(sin.times(size.toPx() / 10f))
                        )
                    )
                    if (style.needleTipHasCircle) {
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
    size: Dp,
    textMeasurer: TextMeasurer,
    valueRange: ClosedFloatingPointRange<Float>,
    hasNumbers: Boolean
) {
    val startRatio = size.toPx().div(9f)
    for (value in valueRange.start.toInt()..valueRange.endInclusive.toInt()) {
        val degree = translate(
            value.toFloat(),
            valueRange,
            numerics.startAngle.toFloat()..totalAngle.toFloat()
        ) + numerics.startAngle
        val degreeInt = degree.toInt()
        val isMark = value % numerics.smallTicksStep == 0
        val isPoint = isMark && (value % numerics.bigTicksStep == 0)
        val isStartOrEnd = isPoint && (degreeInt == numerics.startAngle || degreeInt == totalAngle)
        val endRatio = if (isPoint) size.toPx().div(4f) else size.toPx().div(4.5f)
        val width = if (isPoint) size.div(500f).toPx() else size.div(700f).toPx()
        val markPointColor = if (isPoint) colors.bigTicks else colors.smallTicks

        val radian = Math.toRadians(degree.toDouble())
        val cos = cos(radian).toFloat()
        val sin = sin(radian).toFloat()
        val x = translate(cos, -1f..1f, 0f..size.toPx())
        val y = translate(sin, -1f..1f, 0f..size.toPx())
        val endOffset = Offset(
            x.minus(cos.times(endRatio)),
            y.minus(sin.times(endRatio))
        )
        if (isMark) {
            drawLine(
                color = markPointColor,
                strokeWidth = if (isStartOrEnd) width.times(4) else width,
                cap = StrokeCap.Butt,
                start = Offset(
                    x.minus(cos.times(startRatio)),
                    y.minus(sin.times(startRatio))
                ),
                end = endOffset
            )
        }
        if (hasNumbers && isPoint) {
            val textSizeFactor = 30f
            val textStyle = TextStyle(
                color = colors.bigTicksLabels,
                fontSize = size.toSp() / textSizeFactor
            )
            val textLayout = textMeasurer.measure("$value", textStyle)
            var textOffset = endOffset.minus(
                Offset(
                    textLayout.size.width.toFloat() / 2,
                    textLayout.size.height.toFloat() / 2
                )
            )
            textOffset = textOffset.plus(
                Offset(-1 * textSizeFactor * cos, -1 * textSizeFactor * sin)
            )
            drawText(
                textMeasurer = textMeasurer,
                text = "$value",
                topLeft = textOffset,
                style = textStyle
            )
        }
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
    val alpha = value / valueRange.endInclusive
    drawArc(
        color = onArcColor,
        alpha = if (hasProgressiveAlpha && alpha in 0f..1f) alpha else 1f,
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