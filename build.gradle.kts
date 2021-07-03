import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    val kotlin_version: String by extra
    val gradle_android_version: String by extra

    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        classpath("com.android.tools.build:gradle:$gradle_android_version")
    }
}

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        google()
    }
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions.jvmTarget = "1.8"
        kotlinOptions { freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn" }
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}

