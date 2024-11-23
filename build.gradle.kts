// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    kotlin("plugin.serialization") version "2.0.21"


    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false

    id("com.google.gms.google-services") version "4.3.15" apply false
    id("com.google.firebase.crashlytics") version "2.9.9" apply false

}