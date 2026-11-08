
import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL

group = "no.jksolbakken"
version = System.getenv("PROJ_VERSION") ?: "notimportant"

repositories {
    mavenCentral()
}

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.javadoc)
}

dependencies {
    testImplementation(libs.test.junit5)
    testImplementation(kotlin("test"))

    testRuntimeOnly(libs.test.junit.platform)
}

kotlin {
    jvmToolchain(25)
}

tasks {
    register<Jar>("generateJavadocJar") {
        description = "A Javadoc JAR containing Dokka Javadoc"
        from(dokkaGeneratePublicationJavadoc.flatMap { it.outputDirectory })
        archiveClassifier.set("javadoc")
    }

    withType<Test> {
        useJUnitPlatform()
        testLogging {
            showExceptions = true
        }
        testLogging {
            exceptionFormat = FULL
        }
    }

    withType<Wrapper> {
        gradleVersion = "9.8.2"
    }

    withType<Javadoc> {
        destinationDir = file("${layout.buildDirectory.get()}/docs/javadoc")
        include("no/jksolbakken/*")
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "no.jksolbakken"
            artifactId = "semver"
            version = version
            pom {
                name = "semver"
                description = "Library for working with semantic versioning"
                url.set("https://github.com/jksolbakken/semver")
                licenses {
                    license {
                        name = "MIT"
                        url = "https://en.wikipedia.org/wiki/MIT_License"
                    }
                }
                developers {
                    developer {
                        id.set("jksolbakken")
                    }
                }
                scm {
                    url.set("https://github.com/jksolbakken/semver")
                }
            }

            from(components["java"])
        }
    }
}
