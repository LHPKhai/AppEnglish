plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.learningenglishapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.learningenglishapp"
        minSdk = 24
        targetSdk = 35
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.play.services.tasks)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.picasso)
    implementation(libs.cardview)
    implementation(libs.opencsv)
    implementation(libs.gson)
    implementation(libs.android.mail)
    implementation(libs.android.activation)
    implementation(libs.sdp.android)
    implementation(libs.easy.flip.view)
}