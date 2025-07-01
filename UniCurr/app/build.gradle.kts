plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.hackathon.unicurr"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.hackathon.unicurr"
        minSdk = 24
        targetSdk = 36
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
        viewBinding = true
    }
}

dependencies {
    // Core Libraries
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Room Database
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)

    // Retrofit for API Calls
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Lifecycle ViewModel & LiveData
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)

    // Testing Libraries
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}