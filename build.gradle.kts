plugins {
//    id("build-logic.primitive.kotlin") version "unspecified"
    id("build-logic.primitive.kotlin") version "unspecified"
    id("org.jetbrains.dokka") version "1.8.20"
    id("org.jmailen.kotlinter") version "3.8.0"
    id("com.palantir.git-version") version "0.15.0"
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.dokka")
    apply(plugin = "com.palantir.git-version")
    apply(plugin = "org.jmailen.kotlinter")
}