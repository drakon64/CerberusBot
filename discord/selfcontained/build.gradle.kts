plugins {
    kotlin("multiplatform") version "1.7.21"
    id("io.ktor.plugin") version "2.1.3"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.21"
}

group = "cloud.drakon"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    //    js {
    //        nodejs()
    //        useCommonJs()
    //    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("io.ktor:ktor-server-core:2.1.3")
                implementation("io.ktor:ktor-server-config-yaml:2.1.3")
                implementation("io.ktor:ktor-server-cio:2.1.3")
                implementation("io.ktor:ktor-server-content-negotiation:2.1.3")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.1.3")
                implementation("ch.qos.logback:logback-classic:1.4.5")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("io.ktor:ktor-server-test-host:2.1.3")
                implementation("org.jetbrains.kotlin:kotlin-test-junit:1.7.21")
            }
        }
        val jvmMain by getting
        val jvmTest by getting

        //        val jsMain by getting
        //        val jsTest by getting
    }
}

application {
    mainClass.set("cloud.drakon.tempestbot.ApplicationKt")
}

ktor {
    fatJar {
        archiveFileName.set("TempestBot-selfcontained.jar")
    }
}
