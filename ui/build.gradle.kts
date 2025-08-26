
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

group = "com.osg.openanimation.core"

kotlin {
    js(IR){
        browser()
        binaries.executable()
    }
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    iosArm64()
    iosSimulatorArm64()
    jvm{
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    sourceSets {
        commonMain{
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
            dependencies {
                implementation(projects.core.data)
                implementation(projects.compottie.compottieLite)

                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.material3AdaptiveNavigationSuite)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)

                implementation(libs.adaptive)
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
                api(libs.koin.annotations)
            }
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

ksp {
    arg("KOIN_USE_COMPOSE_VIEWMODEL","true")
    arg("KOIN_CONFIG_CHECK","true")
    arg("KOIN_DEFAULT_MODULE","false")
}

dependencies {
    add("kspCommonMainMetadata", libs.koin.ksp.compiler)
}

project.tasks.withType(KotlinCompilationTask::class.java).configureEach {
    if(name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}


android {
    namespace = "com.osg.openanimation.${project.name}"
    compileSdk = libs.versions.android.targetSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}