import groovy.lang.Closure
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    id("build-logic.primitive.kotlin")
    id("java")
    id("org.jetbrains.dokka") version "1.8.20"
    id("org.jmailen.kotlinter") version "3.8.0"
    id("com.palantir.git-version") version "0.15.0"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

val gitVersion: Closure<String> by extra

group = "com.github.hirotask.mc1171"
version = gitVersion()
base.archivesName.set("NinjaOni")

val shadowImplementation: Configuration by configurations.creating
configurations["implementation"].extendsFrom(shadowImplementation)

val pluginVersion: String by project.ext

repositories {
    mavenCentral()
    maven {
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
    maven {
        url = uri("https://repo.dmulloy2.net/repository/public/")
    }
    maven {
        name = "md_5-public"
        url = uri("https://repo.md-5.net/content/groups/public/")
    }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:${pluginVersion}-R0.1-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.22")
    annotationProcessor("org.projectlombok:lombok:1.18.22")
    testCompileOnly("org.projectlombok:lombok:1.18.22")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.22")
    implementation("dev.jorel:commandapi-bukkit-plugin:10.1.2")
    compileOnly(group="com.comphenix.protocol", name="ProtocolLib", version="4.7.0")
}

val targetJavaVersion = 16

java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible()) {
        options.release.set(targetJavaVersion)
    }
}

tasks.named("build") {
    dependsOn("shadowJar")
}

configure<BukkitPluginDescription> {
    main = "com.github.hirotask.ninjaoni.NinjaOni"
    version = gitVersion()
    apiVersion = "1." + pluginVersion.split(".")[1]
    author = "hirotask"
}
