plugins {
    id("com.github.johnrengelman.shadow").version("8.1.1")
}

tasks.withType<AbstractArchiveTask>().configureEach {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}
