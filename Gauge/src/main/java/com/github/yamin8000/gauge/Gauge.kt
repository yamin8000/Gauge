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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.rotate
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

@Preview(showBackground = true)
@Composable
fun GaugePreview() {
    Surface {
        Gauge(
            size = 200.dp,
            gaugeStart = 150,
            gaugeEnd = 390,
            gaugeStep = 2
        )
    }
}

@Composable
fun Gauge(
    size: Dp,
    gaugeStart: Int,
    gaugeEnd: Int,
    gaugeStep: Int
) {
    val colors = MaterialTheme.colorScheme
    Canvas(
        modifier = Modifier
            .width(size)
            .height(size),
        onDraw = {
            drawRing(
                diameter = size,
                color = colors.primary,
                ringFraction = .05f,
                offset = center
            )
            drawRing(
                diameter = size / 10,
                color = colors.secondary,
                ringFraction = .3f,
                offset = center
            )
            drawCircle(
                color = colors.tertiary,
                radius = size.toPx() / 50,
                center = center
            )

            for (i in gaugeStart..gaugeEnd step gaugeStep) {
                val radian = Math.toRadians(i.toDouble())
                val x = translate(cos(radian).toFloat(), -1f..1f, 0f..size.toPx())
                val y = translate(sin(radian).toFloat(), -1f..1f, 0f..size.toPx())
                drawLine(
                    color = colors.primary,
                    start = Offset(
                        x.minus(cos(radian).toFloat() * 150f),
                        y.minus(sin(radian).toFloat() * 150f)
                    ),
                    end = center.copy(
                        center.x.plus(cos(radian).toFloat() * 200f),
                        center.y.plus(sin(radian).toFloat() * 200f)
                    )
                )
            }
        }
    )
}

@Composable
fun Ring(
    modifier: Modifier = Modifier,
    diameter: Dp = 100.dp,
    @FloatRange(from = 0.0, 1.0)
    ringSize: Float = .1f,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Canvas(
        modifier = modifier.size(diameter),
        onDraw = {
            drawRing(
                color = color,
                diameter = diameter,
                ringFraction = ringSize
            )
        }
    )
}

fun DrawScope.drawRing(
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

@Composable
fun Gauge(
    progress: Number,
    modifier: Modifier = Modifier,
    label: String = "",
    unit: String = "",
    range: IntRange = 0..100,
    totalSize: Dp = 200.dp,
    markersColor: Color = MaterialTheme.colorScheme.tertiary,
    onColor: Color = MaterialTheme.colorScheme.primary,
    offColor: Color = MaterialTheme.colorScheme.primaryContainer,
    arcColorProvider: (Color) -> Color = { generalColorProvider(progress, range, it) }
) {
    val intProgress = progress.toInt()
    val normalProgress = when {
        intProgress in range -> intProgress
        intProgress < range.first -> 0
        intProgress > range.last -> range.last
        else -> -1
    }
    val startAngle = 135f
    val totalDegrees = 270

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Box(
            modifier = modifier,
            content = {
                GaugeText(
                    text = progress.toString(),
                    parentSize = totalSize,
                    modifier = Modifier.align(Alignment.Center)
                )
                GaugeText(
                    text = label,
                    parentSize = totalSize,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
                GaugeText(
                    text = unit,
                    parentSize = totalSize,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 32.dp)
                )
                GaugeText(
                    range.first.toString(),
                    parentSize = totalSize,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(
                            bottom = 16.dp,
                            start = 24.dp
                        )
                )
                GaugeText(
                    range.last.toString(),
                    parentSize = totalSize,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(
                            bottom = 16.dp,
                            end = 2.dp
                        )
                )
                Canvas(
                    modifier = modifier
                        .padding(4.dp)
                        .size(totalSize)
                        .aspectRatio(1f),
                    onDraw = {
                        val canvasWidth = size.width
                        val canvasHeight = size.height

                        drawIntoCanvas {
                            for (degree in -90..180 step 4) {
                                val endOffset =
                                    if (degree % 5 == 0) center.times(.4f)
                                    else center.times(.5f)

                                it.save()
                                it.rotate(
                                    degree.toFloat(),
                                    canvasWidth / 2f,
                                    canvasHeight / 2f
                                )
                                drawLine(
                                    color = markersColor,
                                    start = center.times(.65f),
                                    end = endOffset,
                                    strokeWidth = totalSize.value / 200,
                                    cap = StrokeCap.Butt
                                )
                                it.restore()
                            }
                        }

                        val arcStyle =
                            Stroke(width = canvasWidth / 20, miter = 0f, cap = StrokeCap.Butt)
                        val arcTopLeft = Offset(size.width * .1f, this.size.height * .1f)
                        val arcSize = size.times(.8f)
                        drawArc(
                            color = offColor,
                            startAngle = startAngle,
                            sweepAngle = totalDegrees.toFloat(),
                            useCenter = false,
                            style = arcStyle,
                            size = arcSize,
                            topLeft = arcTopLeft
                        )
                        drawArc(
                            color = arcColorProvider(onColor),
                            startAngle = startAngle,
                            sweepAngle = totalDegrees.toFloat() * normalProgress / range.last,
                            useCenter = false,
                            style = arcStyle,
                            size = arcSize,
                            topLeft = arcTopLeft
                        )
                    }
                )
            }
        )
    }
}

@Composable
private fun GaugeText(
    text: String,
    parentSize: Dp,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        maxLines = 1,
        modifier = modifier,
        fontSize = (parentSize.value / 8f).sp
    )
}

fun generalColorProvider(
    input: Number,
    range: IntRange,
    default: Color
): Color {
    val oneFour = range.last / 4
    val x = when (input.toInt()) {
        in range.first until oneFour -> Color(0xFFF44336)
        in oneFour until oneFour * 2 -> Color(0xFFFFC107)
        in oneFour * 2 until oneFour * 3 -> Color(0xFF8BC34A)
        in oneFour * 3..range.last -> Color(0xFF009688)
        else -> default
    }
    return x
}