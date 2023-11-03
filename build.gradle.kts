// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }
    dependencies {
        classpath("com.google.gms:google-services:4.3.15")
        classpath("io.github.gmazzo.test.aggregation:plugin:2.1.1")
    }
}

plugins {
    id("com.android.application") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
}

apply(plugin = "io.github.gmazzo.test.aggregation.coverage")
