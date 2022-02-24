plugins {
    `java-library`
    kotlin("jvm")
    `maven-publish`
}

java {
    withJavadocJar()
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
dependencies{
// nothing here
}

publishing {
    publications {
        create("maven", MavenPublication::class) {
            afterEvaluate {
                from(components["java"])
            }
        }
    }
}
