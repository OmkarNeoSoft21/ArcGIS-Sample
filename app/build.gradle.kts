plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp.kotlin.android)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.kotlinx.serialisation)
}

android {
    namespace = "com.bm.arcgis_sample"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.bm.arcgis_sample"
        minSdk = 26
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
    kotlinOptions {
        jvmTarget = "11"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.12"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    //ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    //ViewModel utilities for Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    //ArcGIS Maps for Kotlin - SDK dependency
    implementation(libs.arcgis.maps.kotlin)

    //ArcGIS Toolkit dependencies
    implementation(platform(libs.arcgis.maps.kotlin.toolkit.bom))
    implementation(libs.arcgis.maps.kotlin.toolkit.geoview.compose)
    implementation(libs.arcgis.maps.kotlin.toolkit.authentication)
    implementation(libs.permissions.compose)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    //Room
    implementation(libs.bundles.room)
    ksp(libs.androidx.room.compiler)

    //Retrofit
    implementation(libs.bundles.retrofit)
    implementation(libs.logging.interceptor)

    //Dagger Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)


}