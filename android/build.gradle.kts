plugins {
    id("com.android.library")
    kotlin("android")
    `maven-publish`
}
//   android specific variables
val compileSdk by extra {31}
val minSdk by extra { 19 }
val targetSdk by extra { 31 }

android {

    compileSdk = extra["compileSdk"] as Int

    defaultConfig {
        minSdk = extra["minSdk"] as Int
        targetSdk = extra["targetSdk"] as Int
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
dependencies {
    api(project(":core"))
}
//from (android.sourceSets.findByName("main")!!.java.srcDirs)
//classifier="fucking-src"
publishing {
    publications {
        create("maven", MavenPublication::class) {
            afterEvaluate {
                from(components["release"])
            }
        }
    }
}