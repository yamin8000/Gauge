/*
 *     Gauge/Gauge
 *     build.gradle.kts Copyrighted by Yamin Siahmargooei at 2023/10/24
 *     build.gradle.kts Last modified copyright at 2023/10/24
 *     This file is part of Gauge/Gauge.
 *     Copyright (C) 2023  Yamin Siahmargooei
 *
 *     Gauge/Gauge is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Gauge/Gauge is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Gauge.  If not, see <https://www.gnu.org/licenses/>.
 */

plugins {
    id("com.android.application") version "8.6.1" apply false
    id("com.android.library") version "8.6.1" apply false
    id("org.jetbrains.kotlin.android") version "2.2.21" apply false
    id("com.vanniktech.maven.publish") version "0.29.0"
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.20" apply false
}