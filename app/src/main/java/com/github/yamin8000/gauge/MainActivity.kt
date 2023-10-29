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
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
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
                            modifier = Modifier.padding(16.dp)
                        ) {
                            val configuration = LocalConfiguration.current
                            val screenWidth = configuration.screenWidthDp.dp
                            var value by remember { mutableFloatStateOf(0f) }
                            var totalSize by remember { mutableFloatStateOf(350f) }
                            val valueRange = 0f..220f
                            Gauge(
                                value = value,
                                totalSize = totalSize.dp,
                                numerics = GaugeNumerics(
                                    startAngle = 120,
                                    sweepAngle = 300,
                                    valueRange = valueRange,
                                    bigTicksStep = 20,
                                    smallTicksStep = 2
                                ),
                                style = GaugeStyle(
                                    hasBorder = true,
                                    needleTipHasCircle = true
                                )
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
                        }
                    }
                )
            }
        }
    }
}