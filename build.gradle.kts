
val kotlin_version: String by project


plugins {
    kotlin("jvm") version "2.0.20"
    id("io.ktor.plugin") version "3.0.0-rc-2"
    kotlin("plugin.serialization") version "1.8.0"  // Добавьте этот плагин
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

    implementation("com.google.firebase:firebase-admin:9.4.0")


    implementation("io.ktor:ktor-server-netty:2.3.5") // Версия может быть другой
    implementation("io.ktor:ktor-server-core:2.3.5")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.5") // Для JSON
    implementation("ch.qos.logback:logback-classic:1.2.11") // Для логирования
    implementation("io.ktor:ktor-server-content-negotiation:2.3.5") // Для настройки сериализации

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")  // Для сериализации

}
