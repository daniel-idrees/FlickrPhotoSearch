plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.testfeature"
    compileSdk = 35

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    api(libs.junit)
    api (libs.mockito.kotlin)
    api(libs.mockito.core)
    api(libs.mockito.inline)
    api( libs.kotlinx.coroutines.test)
    api (libs.kotest.runner.junit5)
    api (libs.kotest.assertions.core)
    api (libs.kotest.property)
    api(libs.turbine)

    api(libs.androidx.core.ktx)


    api(libs.androidx.junit)
    api (libs.mockito.android)
}
