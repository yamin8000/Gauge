/*
 *     Gauge/Gauge.Gauge
 *     build.gradle.kts Copyrighted by Yamin Siahmargooei at 2023/10/24
 *     build.gradle.kts Last modified copyright at 2023/10/24
 *     This file is part of Gauge/Gauge.Gauge.
 *     Copyright (C) 2023  Yamin Siahmargooei
 *
 *     Gauge/Gauge.Gauge is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Gauge/Gauge.Gauge is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Gauge.  If not, see <https://www.gnu.org/licenses/>.
 */

plugins {
    id("com.android.library")
}

android {
    namespace = "com.github.yamin8000.gauge"
    compileSdk = 33

    defaultConfig {
        minSdk = 24
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }

}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.material3:material3")
//    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.ui:ui")
//    implementation("androidx.compose.material3:material3-window-size-class")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

}