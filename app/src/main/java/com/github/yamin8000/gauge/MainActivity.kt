/*
 *     Gauge/Gauge.app.main
 *     MainActivity.kt Copyrighted by Yamin Siahmargooei at 2023/10/24
 *     MainActivity.kt Last modified copyright at 2023/10/24
 *     This file is part of Gauge/Gauge.app.main.
 *     Copyright (C) 2023  Yamin Siahmargooei
 *
 *     Gauge/Gauge.app.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Gauge/Gauge.app.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Gauge.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.yamin8000.gauge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.github.yamin8000.gauge.main.Gauge
import com.github.yamin8000.gauge.main.GaugeNumerics
import com.github.yamin8000.gauge.ui.color.GaugeArcColors
import com.github.yamin8000.gauge.ui.style.GaugeArcStyle
import com.github.yamin8000.gauge.ui.style.GaugeStyle
import com.github.yamin8000.gauge.ui.theme.GaugeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GaugeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    content = {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            val configuration = LocalConfiguration.current
                            val screenWidth = configuration.screenWidthDp.dp
                            var value by remember { mutableFloatStateOf(15f) }
                            var totalSize by remember { mutableFloatStateOf(350f) }
                            var strokeWidth by remember { mutableFloatStateOf(35f) }
                            val valueRange = 10f..20f
                            Gauge(
                                value = value,
                                totalSize = totalSize.dp,
                                numerics = GaugeNumerics(
                                    startAngle = 120,
                                    sweepAngle = 300,
                                    valueRange = valueRange,
                                    bigTicksStep = 2,
                                    smallTicksStep = 1
                                ),
                                style = GaugeStyle(
                                    hasBorder = true,
                                    arcStyle = GaugeArcStyle(hasProgressiveAlpha = false, strokeWidth = strokeWidth)
                                ),
                                arcColorsProvider = { colors, gaugeValue, range ->
                                    when (gaugeValue) {
                                        in range.start..range.endInclusive / 4 -> GaugeArcColors(
                                            colors.off,
                                            Color.Red
                                        )

                                        in range.endInclusive / 4..range.endInclusive / 2 -> GaugeArcColors(
                                            colors.off,
                                            Color.Yellow
                                        )

                                        in range.endInclusive / 2..range.endInclusive * 3 / 4 -> GaugeArcColors(
                                            colors.off,
                                            Color(0xFFFF8000)
                                        )

                                        else -> GaugeArcColors(colors.off, Color.Green)
                                    }
                                }
                            )
                            Text("width: $screenWidth")
                            Text("Value: $value")
                            Slider(
                                value = value,
                                valueRange = valueRange,
                                onValueChange = {
                                    value = it
                                }
                            )
                            Text("Total Size: $totalSize")
                            Slider(
                                value = totalSize,
                                valueRange = 0f..500f,
                                onValueChange = {
                                    totalSize = it
                                }
                            )

                            Text("Arc Stroke Width: $strokeWidth")
                            Slider(
                                value = strokeWidth,
                                valueRange = 10f..60f,
                                onValueChange = {
                                    strokeWidth = it
                                }
                            )
                        }
                    }
                )
            }
        }
    }
}