plugins {
    id("com.android.application")
    kotlin("android") version "1.8.10"
    id("com.google.gms.google-services")
//    id("de.mannodermaus.android-junit5")
}

android {
    namespace = "com.example.voicetrainingapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.voicetrainingapp"
        minSdk = 24
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }

    buildFeatures {
        dataBinding = true
    }

}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment:2.5.3")
    implementation("androidx.navigation:navigation-ui:2.5.3")
    implementation ("com.android.support:support-annotations:28.0.0")

    //jTransforms
    implementation ("com.github.wendykierp:JTransforms:3.1")

    // Firebase SDK
    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
    implementation("com.google.firebase:firebase-database-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-analytics")
    implementation ("com.google.android.gms:play-services-tasks:17.2.1")

    // Firebase UI Library
    implementation("com.firebaseui:firebase-ui-auth:8.0.2")
    implementation("com.firebaseui:firebase-ui-database:8.0.2")

    //Android Plot
    implementation ("com.androidplot:androidplot-core:1.5.10")
    implementation("androidx.annotation:annotation:1.7.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation("junit:junit:4.12")

    //Testing
    // JUnit 4 for unit tests
    implementation("junit:junit:4.13.2")


// AndroidX Test Libraries (for instrumented tests)
    implementation("androidx.test.ext:junit:1.1.5")
    implementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.test.espresso:espresso-intents:3.5.1")
    implementation("androidx.test.uiautomator:uiautomator:2.2.0")
    implementation("androidx.test:runner:1.4.0")
    implementation("androidx.test:rules:1.4.0")
    implementation("androidx.test:core:1.5.0")

//    implementation("org.robolectric:robolectric:4.7.3")

// Mockito for unit tests
    implementation("org.mockito:mockito-inline:4.11.0")

}
