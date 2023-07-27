plugins {
    kotlin("multiplatform") version "1.9.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.0"
}

group = "cloud.drakon"
version = "0.0.1-SNAPSHOT"

kotlin {
    js {
        nodejs {
            binaries.executable()
            useEsModules()
        }
    }

    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation("cloud.drakon:ktdiscord:6.1.0")
                implementation("cloud.drakon:ktxivapi:0.0.1-SNAPSHOT")
                implementation("cloud.drakon:ktuniversalis:3.0.0")

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.2")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
            }
        }
    }
}

repositories {
    mavenCentral()
    mavenLocal()

    maven {
        name = "KtDiscord"
        url = uri("https://maven.pkg.github.com/TempestProject/KtDiscord")
        credentials {
            username = System.getenv("GITHUB_ACTOR")
            password = System.getenv("GITHUB_TOKEN")
        }
    }

    maven {
        name = "KtUniversalis"
        url = uri("https://maven.pkg.github.com/drakon64/KtUniversalis")
        credentials {
            username = System.getenv("GITHUB_ACTOR")
            password = System.getenv("GITHUB_TOKEN")
        }
    }

    maven {
        name = "KtXivApi"
        url = uri("https://maven.pkg.github.com/drakon64/KtXivApi")
        credentials {
            username = System.getenv("GITHUB_ACTOR")
            password = System.getenv("GITHUB_TOKEN")
        }
    }
}
