import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.21"
    application
    distribution
}

group = "engineer.mathsoftware"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.arrow-kt:arrow-core:1.2.0")
    implementation("io.arrow-kt:arrow-fx-coroutines:1.2.0")
    implementation("io.ktor:ktor-server-core:2.3.3")
    implementation("io.ktor:ktor-server-netty:2.3.3")
    implementation("org.slf4j:slf4j-simple:2.0.7")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "19"
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }
}

kotlin {
    jvmToolchain(19)
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
