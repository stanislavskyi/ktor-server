
val kotlin_version: String by project


plugins {
    kotlin("jvm") version "2.0.20"
    id("io.ktor.plugin") version "3.0.0-rc-2"
}

group = "com.example"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:1.5.8")
    implementation("io.ktor:ktor-server-config-yaml")
    testImplementation("io.ktor:ktor-server-test-host-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

//    implementation("com.google.firebase:firebase-admin:8.2.0")
//
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.5.1")
//    implementation("com.google.firebase:firebase-firestore-ktx:24.0.0")


}
