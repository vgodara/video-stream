val serialization_version: String by extra
val ktor_version: String by extra
plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.5.0"
}

group = "com.video.stream"
version = "0.1"
sourceSets {
    getByName("main").java.srcDirs("src/main")
    getByName("test").java.srcDirs("src/test")
}
dependencies{
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serialization_version")
    implementation("io.ktor:ktor-http-jvm:$ktor_version")

}