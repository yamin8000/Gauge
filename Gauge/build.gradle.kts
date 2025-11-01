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

private val artifact = "com.github.yamin8000.gauge"
private val version = "1.0.4"

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.vanniktech.maven.publish")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = artifact
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2025.10.01"))
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
}

mavenPublishing {
    publishToMavenCentral(com.vanniktech.maven.publish.SonatypeHost.S01, true)
    signAllPublications()

    coordinates(artifact, "Gauge", version)

    pom {
        name.set("Gauge")
        description.set("Gauge Composable is a fusion of classic and modern Gauges with some customization options.")
        inceptionYear.set("2023")
        url.set("https://github.com/yamin8000/Gauge")
        licenses {
            license {
                name.set("GPL-3.0 license ")
                url.set("https://www.gnu.org/licenses")
                distribution.set("https://raw.githubusercontent.com/yamin8000/Gauge/master/LICENSE")
            }
        }
        developers {
            developer {
                id.set("yamin8000")
                name.set("Yamin Siahmargooei")
                url.set("https://github.com/yamin8000")
            }
        }
        scm {
            url.set("https://github.com/yamin8000/Gauge")
            connection.set("scm:git:git://github.com/yamin8000/Gauge.git")
            developerConnection.set("scm:git:ssh://git@github.com:yamin8000/Gauge.git")
        }
    }
}
