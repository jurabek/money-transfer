val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val koin_version: String by project

repositories {
    mavenLocal()
    jcenter()
    maven { url = uri("https://kotlin.bintray.com/ktor") }
}

plugins {
    application
    kotlin("jvm") version "1.3.61"
    id("org.jlleitschuh.gradle.ktlint") version "8.2.0"
    id("io.gitlab.arturbosch.detekt") version "1.5.1"
    id("com.github.johnrengelman.shadow") version "5.0.0"
}

group = "com.jurabek"
version = "0.0.1"
application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("org.koin:koin-ktor:$koin_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-jackson:$ktor_version")
    implementation("io.github.microutils:kotlin-logging:1.7.6")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.10.+")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("io.ktor:ktor-server-test-host:$ktor_version")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.3")
    testImplementation("org.mockito:mockito-core:2.+")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.+")
}

kotlin.sourceSets["main"].kotlin.srcDirs("src")
kotlin.sourceSets["test"].kotlin.srcDirs("test")

sourceSets["main"].resources.srcDirs("resources")
sourceSets["test"].resources.srcDirs("testresources")

tasks.withType<Jar> {
    manifest {
        attributes(
            mapOf(
                "Main-Class" to application.mainClassName
            )
        )
    }
}

detekt {
    buildUponDefaultConfig = true
    reports {
        xml {
            enabled = false
        }
        html {
            enabled = true
            destination = file("build/reports/detekt.html")
        }
        txt {
            enabled = false
        }
    }
}