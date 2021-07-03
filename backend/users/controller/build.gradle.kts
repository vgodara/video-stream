val coroutines_version: String by extra
val serialization_version: String by extra
val kotest_version: String by extra
val mockk_version: String by extra
plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.5.0"
}
group = "com.video.stream.backend.users"
version = "0.1"
sourceSets {
    getByName("main").java.srcDirs("src/main")
    getByName("test").java.srcDirs("src/test")
}

dependencies {
    implementation(project(":common:response"))
    implementation(project(":backend:common"))
    implementation(project(":backend:users:common"))
    implementation(project(":backend:users:usecase"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serialization_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
}
