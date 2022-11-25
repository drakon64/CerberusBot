import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
    id("io.ktor.plugin") version "2.1.3"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.21"
}

group = "cloud.drakon"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("io.ktor:ktor-server-core:2.1.3")
    implementation("io.ktor:ktor-server-config-yaml:2.1.3")
    implementation("io.ktor:ktor-server-cio:2.1.3")
    implementation("io.ktor:ktor-server-content-negotiation:2.1.3")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.1.3")
    implementation("ch.qos.logback:logback-classic:1.4.5")

    implementation("cloud.drakon:tempest:0.0.1-SNAPSHOT")

    testImplementation(kotlin("test"))
}

application {
    mainClass.set("cloud.drakon.tempestbot.ApplicationKt")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }
    test {
        useJUnitPlatform()
    }
}
