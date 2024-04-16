plugins {
    id("com.android.application")
    kotlin("android") version "1.8.10"
}

android {
    namespace = "com.example.voicetrainingapp"
    compileSdk = 33

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

    //Google firebase
//    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))

    //Android Plot
    implementation ("com.androidplot:androidplot-core:1.5.10")

    //Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
//apply(plugin = "com.google.gms.google-services")
