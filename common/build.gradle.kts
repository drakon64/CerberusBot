plugins {
    kotlin("multiplatform") version "1.7.21"
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
                implementation("aws.sdk.kotlin:translate:0.17.12-beta")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting
        val jvmTest by getting

        //        val jsMain by getting
        //        val jsTest by getting
    }
}
