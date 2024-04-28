buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath ("com.android.tools.build:gradle:8.1.1")
//        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.10")
        // The google-services plugin is required to parse the google-services.json file
        classpath ("com.google.gms:google-services:4.4.1")
    }
}