import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.21"
    kotlin("plugin.serialization") version "1.9.23"
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
    application
    distribution
}

group = "engineer.mathsoftware"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-client-cio-jvm:2.3.9")
    val ktorVersion = "2.3.9"

    implementation("io.arrow-kt:arrow-core:1.2.0")
    implementation("io.arrow-kt:arrow-fx-coroutines:1.2.0")
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-client-json:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-client-logging:$ktorVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation("org.slf4j:slf4j-simple:2.0.7")
    implementation("org.jsoup:jsoup:1.17.2")
    testImplementation(kotlin("test"))
}

javafx {
    version = "22.0.1"
    modules = listOf("javafx.controls", "javafx.swing").toMutableList()
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "21"
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("MainKt")
}

distributions {
    main {
        contents {
            from(rootDir.resolve("src/main")) {
                include("files/**")
            }
        }
    }
}
