plugins {

    // default project plugins
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    // required by ROOM (SQLite) database
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

    // YAML files support
    implementation(libs.snakeyaml)

    // Jackson and needed formats support
    implementation(libs.jackson.databind)
    implementation(libs.jackson.module.kotlin)
    implementation(libs.jackson.dataformat.yaml)

    // Google GSON
    implementation(libs.gson)

    // SQLite database
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler.v260)
    annotationProcessor(libs.androidx.room.compiler)
    implementation(libs.androidx.room.guava)
    implementation(libs.androidx.room.ktx)

    // DataStore (settings storage)
    implementation(libs.androidx.datastore.preferences)

    // viewModels support
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Logging
    implementation(libs.logger)

    // Event Bus
    implementation(libs.eventbus)
}
