plugins {
    alias(libs.plugins.android)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "dev.cankolay.wakeup"
    compileSdk = libs.versions.compileSdk
        .get()
        .toInt()

    defaultConfig {
        applicationId = "dev.cankolay.wakeup"

        minSdk =
            libs.versions.minSdk
                .get()
                .toInt()
        targetSdk =
            libs.versions.compileSdk
                .get()
                .toInt()

        versionCode = 1
        versionName = "1.0"
    }

    sourceSets.all {
        kotlin.srcDir("src/$name/kotlin")
    }

    signingConfigs {
        named("debug") {
            storeFile = rootProject.file("app-key.keystore")
            storePassword = "android"
            keyAlias = "android"
            keyPassword = "android"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false

            signingConfig = signingConfigs.getByName("debug")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true

            signingConfig = signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlinOptions {
        jvmTarget = "21"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.bundles.core)

    implementation(libs.bundles.hilt)
    ksp(libs.hilt.compiler)

    implementation(libs.kotlinx.serialization.json)

    implementation(libs.appcompat)

    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)

    implementation(libs.compose.navigation)

    implementation(libs.bundles.lifecycle)

    implementation(libs.bundles.ktor)

    implementation(libs.datastore)
}