import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.10"
    application
}

group = "me.logan"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test-junit"))
    implementation("org.apache.lucene:lucene-core:7.1.0")
    implementation("org.apache.lucene:lucene-benchmark:7.1.0")
    implementation("uk.ac.gla.dcs.terrierteam:jtreceval:0.0.5")
    implementation("commons-io:commons-io:2.4")
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClassName = "MainKt"
}