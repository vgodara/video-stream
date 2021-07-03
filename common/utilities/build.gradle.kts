plugins {
    kotlin("jvm")
}

group = "com.video.stream"
version = "0.1"
sourceSets {
    getByName("main").java.srcDirs("src/main")
    getByName("test").java.srcDirs("src/test")
}
dependencies {
    implementation("com.benasher44:uuid:0.2.4")
    implementation("com.lambdaworks:scrypt:1.4.0")
}