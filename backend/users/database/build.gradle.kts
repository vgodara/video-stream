val coroutines_version: String by extra
val exposed_version: String by extra
val kotlin_version: String by extra
plugins {
    kotlin("jvm")
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
    implementation(project(":backend:users:datasource"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("mysql:mysql-connector-java:5.1.48")
}
