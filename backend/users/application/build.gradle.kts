val ktor_version: String by extra
val kotlin_version: String by extra
val logback_version: String by extra
val coroutines_version: String by extra
val koin_version: String by extra
val kotest_version: String by extra

plugins {
    kotlin("jvm")
    application
}

group = "com.stream.video.users"
version = "0.0.1"
application {
    mainClass.set("com.stream.video.users.application.ApplicationKt")
}
sourceSets {
    getByName("main").java.srcDirs("src/main")
    getByName("test").java.srcDirs("src/test")
}


dependencies {
    implementation(project(":common:response"))
    implementation(project(":common:utilities"))
    implementation(project(":backend:common"))
    implementation(project(":backend:users:common"))
    implementation(project(":backend:users:usecase"))
    implementation(project(":backend:users:controller"))
    implementation(project(":backend:users:datasource"))
    implementation(project(":backend:users:database"))
    implementation(project(":backend:users:entity"))
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-auth-jwt:$ktor_version")
    implementation("io.ktor:ktor-serialization:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")

    // Koin for Ktor
    implementation("io.insert-koin:koin-ktor:$koin_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")

    testImplementation("io.ktor:ktor-server-test-host:$ktor_version")
    testImplementation ("io.kotest:kotest-runner-junit5:$kotest_version")

}

tasks.withType<Test> {
    useJUnitPlatform()
}