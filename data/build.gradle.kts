import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.jetbrainsKotlinKapt)
    alias(libs.plugins.google.dagger)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use { inputStream ->
        localProperties.load(inputStream)
    }
}

val apiKey: String =
    System.getenv("API_KEY") ?: localProperties.getProperty("API_KEY", "")

android {
    namespace = "com.example.data"
    compileSdk = 35

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField("String", "API_KEY", "\"${apiKey}\"")
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.retrofit.core)
    implementation(libs.logging.interceptor)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit.kotlin.serialization)

    implementation(libs.hilt.android)
    kapt(libs.androidx.hilt.compiler)
    kapt(libs.hilt.compiler)
    testImplementation(project(":test-util"))
}