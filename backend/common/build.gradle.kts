plugins {
    kotlin("jvm")
}

group = "com.video.stream.backend"
version = "0.1"
sourceSets {
    getByName("main").java.srcDirs("src/main")
    getByName("test").java.srcDirs("src/test")
}
val coroutines_version: String by extra
