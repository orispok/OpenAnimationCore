
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.stability.analyzer)
    alias(libs.plugins.koin.compiler)
}

group = "com.osg.openanimation.core"

kotlin {
    js(IR){
        browser()
        binaries.executable()
    }
    android {
        namespace = "com.osg.openanimation.${project.name}"
        compileSdk = libs.versions.android.targetSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        withJava()
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
        androidResources {
            enable = true
        }
    }
    iosArm64()
    iosSimulatorArm64()
    jvm()
    sourceSets {
        commonMain{
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
            dependencies {
                implementation(projects.core.data)
                implementation(libs.compottie.lite)

                implementation(libs.runtime)
                implementation(libs.foundation)
                implementation(libs.ui)
                implementation(libs.components.resources)
                implementation(libs.ui.tooling.preview)

                implementation(libs.material3)
                implementation(libs.material3.adaptive)
                implementation(libs.material3.navigation.suite)
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.serialization.core)
                implementation(libs.kotlinx.serialization.protobuf)
                implementation(libs.androidx.navigation.compose)
                implementation(libs.material.icons.core)
                implementation(libs.androidx.lifecycle.viewmodel.compose)
                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)
                api(libs.kotlinx.collections.immutable)
                api(libs.koin.annotations)
            }
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}