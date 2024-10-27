plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
//    implementation(libs.navigation.fragment.ktx)
//    implementation(libs.navigation.ui.ktx)
//    alias(libs.plugins.kotlin.kapt)
    //alias(libs.plugins.kotlinKapt)
    //kotlin("kapt") version "2.0.21"
//    id("com.google.devtools.ksp")
    alias(libs.plugins.org.jetbrains.kotlin.kapt)
}

android {
    namespace = "space.kovo.nocnyprud2"
    compileSdk = 34

    defaultConfig {
        applicationId = "space.kovo.nocnyprud2"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.7"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // default project dependencies
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
    // custom added dependencies
    implementation(libs.snakeyaml)
    implementation(libs.jackson.databind)
    implementation(libs.jackson.module.kotlin)
    implementation(libs.jackson.dataformat.yaml)
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler.v260)
    annotationProcessor(libs.androidx.room.compiler)
    implementation(libs.androidx.room.guava)

}
