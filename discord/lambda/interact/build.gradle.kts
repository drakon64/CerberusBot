plugins {
    kotlin("js") version "1.7.21"
}

group = "cloud.drakon"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

kotlin {
    js {
        nodejs()
        useCommonJs()
    }
}
