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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.yamin8000.gauge.ui.color.GaugeArcColors
import com.github.yamin8000.gauge.ui.color.GaugeColors
import com.github.yamin8000.gauge.ui.color.GaugeNeedleColors
import com.github.yamin8000.gauge.ui.color.GaugeTicksColors
import com.github.yamin8000.gauge.ui.style.GaugeArcStyle
import com.github.yamin8000.gauge.ui.style.GaugeNeedleStyle
import com.github.yamin8000.gauge.ui.style.GaugeStyle
import com.github.yamin8000.gauge.util.translate
import com.github.yamin8000.gauge.util.translate2
import java.text.DecimalFormat
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

/**
 * Gauge Composable is a fusion of classic and modern Gauges with some
 * customization options.
 *
 * @param value current value of the [Gauge], this value directly affects
 *    Gauge's arc and Gauge's needle style
 * @param modifier refer to [Modifier]
 * @param valueUnit unit of the Gauge's value like %, km/h or etc
 * @param decimalFormat decimal formatter for value text
 * @param numerics refer to [GaugeNumerics]
 * @param style refer to [GaugeStyle]
 * @param ticksColorProvider a lambda for fine tuning of individual tick's
 *    color
 * @param arcColorsProvider a lambda for fine tune of arc colors
 * @throws IllegalArgumentException when some parameters are inconsistent
 *    with the design
 */
@Composable
fun Gauge(
    value: Float,
    modifier: Modifier = Modifier,
    valueUnit: String = "",
    decimalFormat: DecimalFormat = DecimalFormat().apply { maximumFractionDigits = 2 },
    numerics: GaugeNumerics,
    style: GaugeStyle = GaugeStyle(),
    colors: GaugeColors = GaugeColors.defaultColors(),
    ticksColorProvider: (List<Pair<Int, Color>>) -> List<Pair<Int, Color>> = { it },
    arcColorsProvider: (GaugeArcColors, Float, ClosedFloatingPointRange<Float>) -> GaugeArcColors = { _, _, _ -> colors.arc }
) {
    require(value in numerics.valueRange) { "Gauge value: $value is out of Gauge Value range ${numerics.valueRange}" }
    require(numerics.sweepAngle in 1..360) { "Sweep angle: ${numerics.sweepAngle} must be from 1 to 360" }

    BoxWithConstraints(
        modifier = modifier
            .padding(8.dp)
            .aspectRatio(1f),
        content = {
            val textMeasurer = rememberTextMeasurer()
            val density = LocalDensity.current
            val size = with(density) { max(maxWidth.toPx(), maxHeight.toPx()).toDp() }
            val totalAngle = numerics.startAngle + numerics.sweepAngle
            val arcSizeFraction = remember { .9f }

            Canvas(
                modifier = Modifier.size(size),
                onDraw = {
                    val safeSize = this@Canvas.size.maxDimension - style.borderWidth
                    val safeOffset = Offset(style.borderWidth / 2f, style.borderWidth / 2f)
                    if (style.hasBorder) {
                        drawCircle(
                            color = colors.border,
                            center = center,
                            style = Stroke(style.borderWidth),
                            radius = safeSize / 2f
                        )
                    }
                    if (style.hasValueText) {
                        drawCompatibleText(
                            textMeasurer = textMeasurer,
                            text = decimalFormat.format(value).trim(),
                            topLeft = center.plus(Offset(0f, size.toPx() / 8)),
                            color = colors.value,
                            totalSize = safeSize.toDp()
                        )
                        drawCompatibleText(
                            textMeasurer = textMeasurer,
                            text = valueUnit.trim(),
                            topLeft = center.plus(Offset(0f, size.toPx() / 7)),
                            color = colors.value,
                            totalSize = safeSize.toDp()
                        )
                    }
                    drawTicks(
                        offset = safeOffset,
                        numerics = numerics,
                        totalAngle = totalAngle,
                        colors = colors.ticks,
                        size = safeSize.toDp(),
                        textMeasurer = textMeasurer,
                        hasNumbers = style.arcStyle.bigTicksHasLabels,
                        ticksColorProvider = ticksColorProvider
                    )
                    if (style.arcStyle.hasArcs) {
                        drawArcs(
                            offset = safeOffset,
                            size = safeSize.toDp(),
                            style = style.arcStyle,
                            arcSizeFraction = arcSizeFraction,
                            colors = colors.arc,
                            numerics = numerics,
                            value = value,
                            valueRange = numerics.valueRange,
                            totalAngle = totalAngle,
                            arcColorsProvider = arcColorsProvider
                        )
                    }
                    if (style.needleStyle.hasNeedle) {
                        drawNeedle(
                            offset = safeOffset,
                            style = style.needleStyle,
                            colors = colors.needle,
                            size = safeSize.toDp(),
                            value = value,
                            numerics = numerics,
                            totalAngle = totalAngle
                        )
                    }
                    drawCircle(
                        color = colors.centerCircle,
                        radius = safeSize / 50,
                        center = center
                    )
                }
            )
        }
    )
}

private fun DrawScope.drawNeedle(
    offset: Offset,
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
            center = center,
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
    ).plus(offset)
    drawLine(
        color = colors.needle,
        start = center,
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
    offset: Offset,
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
        val tickEndRatio = if (isBigTick) size.toPx().div(5f) else size.toPx().div(6f)
        val width = if (isBigTick) size.div(500f).toPx() else size.div(700f).toPx()
        val tickColor = if (isBigTick) colors.bigTicks
        else ticksColors[value - numerics.valueRange.start.toInt()].second

        val radian = Math.toRadians(degree.toDouble())
        val cos = cos(radian).toFloat()
        val sin = sin(radian).toFloat()
        val x = translate(cos, -1f..1f, 0f..size.toPx())
        val y = translate(sin, -1f..1f, 0f..size.toPx())
        val tickEndOffset = Offset(
            x.minus(cos.times(tickEndRatio)),
            y.minus(sin.times(tickEndRatio))
        )

        if (isSmallTick) {
            drawLine(
                color = tickColor,
                strokeWidth = if (isStartOrEnd) width.times(4) else width,
                cap = StrokeCap.Butt,
                start = Offset(
                    x.minus(cos.times(startRatio)),
                    y.minus(sin.times(startRatio))
                ).plus(offset),
                end = tickEndOffset.plus(offset)
            )
        }
        if (hasNumbers && isBigTick) {
            val textSizeFactor = 15f
            val textStyle = TextStyle(
                color = colors.bigTicksLabels,
                fontSize = size.toSp() / textSizeFactor
            )
            val textLayout = textMeasurer.measure("$value", textStyle)
            val textEndRatio = size.toPx().div(4.25f)
            var textOffset = Offset(
                x.minus(cos.times(textEndRatio)),
                y.minus(sin.times(textEndRatio))
            )
            textOffset = textOffset.minus(
                Offset(
                    textLayout.size.width.toFloat() / 2,
                    textLayout.size.height.toFloat() / 2
                )
            )
            textOffset = textOffset.plus(
                Offset(-1f * textSizeFactor * cos, -1f * textSizeFactor * sin)
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
    offset: Offset,
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

    val strokeWidth = if (style.strokeWidth != null) {
        if (style.strokeWidth < size.toPx() / 15f) style.strokeWidth else size.toPx() / 15f
    } else size.toPx() / 15f

    val arcStroke = Stroke(
        width = strokeWidth,
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
        topLeft = arcTopLeft.plus(offset)
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
        topLeft = arcTopLeft.plus(offset)
    )
}

private fun DrawScope.drawCompatibleText(
    text: String,
    color: Color,
    totalSize: Dp,
    textMeasurer: TextMeasurer,
    topLeft: Offset
) {
    val textSizeFactor = 20f
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