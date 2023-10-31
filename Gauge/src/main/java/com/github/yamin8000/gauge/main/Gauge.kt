/*
 *     Gauge/Gauge.Gauge.main
 *     Gauge.kt Copyrighted by Yamin Siahmargooei at 2023/10/31
 *     Gauge.kt Last modified copyright at 2023/10/31
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
import com.github.yamin8000.gauge.ui.color.GaugeArcColors
import com.github.yamin8000.gauge.ui.color.GaugeNeedleColors
import com.github.yamin8000.gauge.ui.color.GaugeTicksColors
import com.github.yamin8000.gauge.ui.style.GaugeArcStyle
import com.github.yamin8000.gauge.ui.style.GaugeNeedleStyle
import com.github.yamin8000.gauge.ui.style.GaugeStyle
import com.github.yamin8000.gauge.util.translate
import com.github.yamin8000.gauge.util.translate2
import java.text.DecimalFormat
import kotlin.math.cos
import kotlin.math.sin

/**
 * Gauge Composable is a fusion of classic and modern Gauges with some customization options.
 *
 * @param value current value of the [Gauge], this value directly affects Gauge's arc and Gauge's needle style
 * @param modifier refer to [Modifier]
 * @param valueUnit unit of the Gauge's value like %, km/h or etc
 * @param decimalFormat decimal formatter for value text
 * @param totalSize total size of this [Gauge] as a Composable
 * @param borderInset Gauge's inset relative to the border
 * @param numerics refer to [GaugeNumerics]
 * @param style refer to [GaugeStyle]
 * @param borderColor
 * @param centerCircleColor
 * @param needleColors
 * @param arcColors
 * @param ticksColors
 * @param ticksColorProvider a lambda for fine tune of individual tick's color
 * @param arcColorsProvider a lambda for fine tune of arc colors
 *
 * @throws IllegalArgumentException when some parameters are inconsistent with the design
 */
@Composable
fun Gauge(
    value: Float,
    modifier: Modifier = Modifier,
    valueUnit: String = "",
    decimalFormat: DecimalFormat = DecimalFormat().apply { maximumFractionDigits = 2 },
    totalSize: Dp = LocalConfiguration.current.screenWidthDp.dp,
    borderInset: Dp = 4.dp,
    numerics: GaugeNumerics,
    style: GaugeStyle = GaugeStyle(
        borderWidth = totalSize.value / 30f,
        needleStyle = GaugeNeedleStyle(
            ringWidth = totalSize.value / 30f
        )
    ),
    borderColor: Color = MaterialTheme.colorScheme.primaryContainer,
    centerCircleColor: Color = MaterialTheme.colorScheme.tertiary,
    valueColor: Color = MaterialTheme.colorScheme.primary,
    needleColors: GaugeNeedleColors = GaugeNeedleColors(
        needle = MaterialTheme.colorScheme.primary,
        ring = MaterialTheme.colorScheme.tertiaryContainer
    ),
    arcColors: GaugeArcColors = GaugeArcColors(
        off = MaterialTheme.colorScheme.inversePrimary,
        on = MaterialTheme.colorScheme.primary
    ),
    ticksColors: GaugeTicksColors = GaugeTicksColors(
        smallTicks = MaterialTheme.colorScheme.inversePrimary,
        bigTicks = MaterialTheme.colorScheme.primary,
        bigTicksLabels = MaterialTheme.colorScheme.tertiary
    ),
    ticksColorProvider: (List<Pair<Int, Color>>) -> List<Pair<Int, Color>> = { it },
    arcColorsProvider: (GaugeArcColors, Float, ClosedFloatingPointRange<Float>) -> GaugeArcColors = { _, _, _ -> arcColors }
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
            val arcSizeFraction = remember { .9f }
            Canvas(
                modifier = Modifier
                    .width(size)
                    .height(size),
                onDraw = {
                    val borderOffset = Offset(borderInset.toPx() / 2, borderInset.toPx() / 2)
                    if (style.hasBorder) {
                        drawCircle(
                            color = borderColor,
                            center = center,
                            style = Stroke(style.borderWidth)
                        )
                    }
                    if (style.hasValueText) {
                        drawCompatibleText(
                            textMeasurer = textMeasurer,
                            text = "${decimalFormat.format(value)}\n$valueUnit".trim(),
                            topLeft = center.plus(borderOffset).plus(Offset(0f, size.toPx() / 5)),
                            color = valueColor,
                            totalSize = size
                        )
                    }
                    drawTicks(
                        borderOffset = borderOffset,
                        numerics = numerics,
                        totalAngle = totalAngle,
                        colors = ticksColors,
                        size = size - borderInset,
                        textMeasurer = textMeasurer,
                        hasNumbers = style.arcStyle.bigTicksHasLabels,
                        ticksColorProvider = ticksColorProvider
                    )
                    if (style.arcStyle.hasArcs) {
                        drawArcs(
                            borderOffset = borderOffset,
                            size = size - borderInset,
                            style = style.arcStyle,
                            arcSizeFraction = arcSizeFraction,
                            colors = arcColors,
                            numerics = numerics,
                            value = value,
                            valueRange = numerics.valueRange,
                            totalAngle = totalAngle,
                            arcColorsProvider = arcColorsProvider
                        )
                    }
                    if (style.needleStyle.hasNeedle) {
                        drawNeedle(
                            borderOffset = borderOffset,
                            style = style.needleStyle,
                            colors = needleColors,
                            size = size - borderInset,
                            value = value,
                            numerics = numerics,
                            totalAngle = totalAngle
                        )
                    }
                    drawCircle(
                        color = centerCircleColor,
                        radius = (size - borderInset).toPx() / 50,
                        center = center.plus(borderOffset)
                    )
                }
            )
        }
    )
}

private fun DrawScope.drawNeedle(
    borderOffset: Offset,
    style: GaugeNeedleStyle,
    colors: GaugeNeedleColors,
    size: Dp,
    value: Float,
    numerics: GaugeNumerics,
    totalAngle: Int
) {
    if (style.hasRing) {
        drawCircle(
            color = colors.ring,
            center = center.plus(borderOffset),
            style = Stroke(style.ringWidth),
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
    val endOffset = Offset(
        x.minus(cos.times(size.toPx() / 20f)),
        y.minus(sin.times(size.toPx() / 20f))
    ).plus(borderOffset)
    drawLine(
        color = colors.needle,
        start = center.plus(borderOffset),
        strokeWidth = 10f,
        cap = StrokeCap.Round,
        end = endOffset
    )
    if (style.tipHasCircle) {
        drawCircle(
            color = colors.needle,
            radius = size.toPx() / 50,
            center = endOffset
        )
    }
}

private fun DrawScope.drawTicks(
    borderOffset: Offset,
    numerics: GaugeNumerics,
    totalAngle: Int,
    colors: GaugeTicksColors,
    size: Dp,
    textMeasurer: TextMeasurer,
    hasNumbers: Boolean,
    ticksColorProvider: (List<Pair<Int, Color>>) -> List<Pair<Int, Color>>
) {
    val ticksColors = ticksColorProvider(
        buildList {
            (numerics.valueRange.start.toInt()..numerics.valueRange.endInclusive.toInt()).forEach {
                add(it to colors.smallTicks)
            }
        }
    )
    val startRatio = size.toPx().div(40f)
    for (value in numerics.valueRange.start.toInt()..numerics.valueRange.endInclusive.toInt()) {
        val degree = translate(
            value.toFloat(),
            numerics.valueRange,
            numerics.startAngle.toFloat()..totalAngle.toFloat()
        ) + numerics.startAngle
        val degreeInt = degree.toInt()
        val isSmallTick = value % numerics.smallTicksStep == 0
        val isBigTick = isSmallTick && (value % numerics.bigTicksStep == 0)
        val isStartOrEnd =
            isBigTick && (degreeInt == numerics.startAngle || degreeInt == totalAngle)
        val endRatio = if (isBigTick) size.toPx().div(6f) else size.toPx().div(7f)
        val width = if (isBigTick) size.div(500f).toPx() else size.div(700f).toPx()
        val tickColor = if (isBigTick) colors.bigTicks else ticksColors[value].second

        val radian = Math.toRadians(degree.toDouble())
        val cos = cos(radian).toFloat()
        val sin = sin(radian).toFloat()
        val x = translate(cos, -1f..1f, 0f..size.toPx())
        val y = translate(sin, -1f..1f, 0f..size.toPx())
        val endOffset = Offset(
            x.minus(cos.times(endRatio)),
            y.minus(sin.times(endRatio))
        )
        if (isSmallTick) {
            drawLine(
                color = tickColor,
                strokeWidth = if (isStartOrEnd) width.times(4) else width,
                cap = StrokeCap.Butt,
                start = Offset(
                    x.minus(cos.times(startRatio)),
                    y.minus(sin.times(startRatio))
                ).plus(borderOffset),
                end = endOffset.plus(borderOffset)
            )
        }
        if (hasNumbers && isBigTick) {
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
                topLeft = textOffset.plus(borderOffset),
                style = textStyle
            )
        }
    }
}

private fun DrawScope.drawArcs(
    borderOffset: Offset,
    size: Dp,
    style: GaugeArcStyle,
    arcSizeFraction: Float,
    colors: GaugeArcColors,
    numerics: GaugeNumerics,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    totalAngle: Int,
    arcColorsProvider: (GaugeArcColors, Float, ClosedFloatingPointRange<Float>) -> GaugeArcColors
) {
    val arcColors = arcColorsProvider(colors, value, valueRange)
    val arcStroke = Stroke(
        width = size.toPx() / 15f,
        miter = 0f,
        cap = style.cap
    )
    val arcSize = Size(
        size.times(arcSizeFraction).toPx(),
        size.times(arcSizeFraction).toPx()
    )
    val arcTopLeft = Offset(
        size.times((1 - arcSizeFraction) / 2).toPx(),
        size.times((1 - arcSizeFraction) / 2).toPx()
    )
    drawArc(
        color = arcColors.off,
        startAngle = numerics.startAngle.toFloat(),
        sweepAngle = numerics.sweepAngle.toFloat(),
        useCenter = false,
        style = arcStroke,
        size = arcSize,
        topLeft = arcTopLeft.plus(borderOffset)
    )
    val alpha = value / valueRange.endInclusive
    drawArc(
        color = arcColors.on,
        alpha = if (style.hasProgressiveAlpha && alpha in 0f..1f) alpha else 1f,
        startAngle = numerics.startAngle.toFloat(),
        sweepAngle = translate(
            value,
            valueRange.start..valueRange.endInclusive,
            numerics.startAngle.toFloat()..totalAngle.toFloat()
        ),
        useCenter = false,
        style = arcStroke,
        size = arcSize,
        topLeft = arcTopLeft.plus(borderOffset)
    )
}

private fun DrawScope.drawCompatibleText(
    text: String,
    color: Color,
    totalSize: Dp,
    textMeasurer: TextMeasurer,
    topLeft: Offset
) {
    val textSizeFactor = 30f
    val textStyle = TextStyle(
        color = color,
        fontSize = totalSize.toSp() / textSizeFactor
    )
    val textLayout = textMeasurer.measure(text, textStyle)
    val textOffset = topLeft.minus(
        Offset(
            textLayout.size.width.toFloat() / 2,
            textLayout.size.height.toFloat() / 2
        )
    )
    drawText(
        textMeasurer = textMeasurer,
        text = text,
        topLeft = textOffset,
        style = textStyle
    )
}