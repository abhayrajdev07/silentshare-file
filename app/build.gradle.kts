plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.silentshare"
    compileSdk = 34 // Synced with your architecture overview

    defaultConfig {
        applicationId = "com.example.silentshare"
        minSdk = 24
        targetSdk = 34 // Synced with your architecture overview
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }


    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            // Compose requirements
            excludes += "/META-INF/{AL2.0,LGPL2.1}"

            // 🔥 Ktor Server requirements to prevent build crashes
            excludes += "META-INF/INDEX.LIST"
            excludes += "META-INF/DEPENDENCIES"
            excludes += "META-INF/io.netty.versions.properties"
            excludes += "META-INF/versions/9/module-info.class"
        }
    }
}

dependencies {
    // 🔹 AndroidX Core & Lifecycle
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // 🔹 Compose UI
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation("androidx.compose.material:material-icons-extended:1.6.0") // Removed duplicate entry

    // 🔹 Image Loading
    implementation("io.coil-kt:coil-compose:2.6.0")

    // 🔹 Protobuf
    implementation(libs.protolite.well.known.types)
    // 🔥 QR Code Generator & Scanner
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation("com.google.zxing:core:3.4.1")
    // 🔥 KTOR SERVER (LAN CHAT BACKEND)
    implementation("io.ktor:ktor-server-core:2.3.8")
    implementation("io.ktor:ktor-server-cio:2.3.8")
    implementation("io.ktor:ktor-server-websockets:2.3.8")
    implementation("org.slf4j:slf4j-android:1.7.36") // Required to prevent Ktor crash on launch

    // 🔥 OKHTTP (WEBSOCKET CLIENT)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("io.coil-kt:coil-compose:2.4.0")

    // 🔹 Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

}