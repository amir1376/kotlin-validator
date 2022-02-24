plugins {
   `maven-publish`
}
allprojects {
   group = "ir.amirab.validator"
   version = "0.0.1-beta4"
   repositories {
      google()
      mavenCentral()
   }
}
buildscript {
   repositories {
      google()
      mavenCentral()
   }
   val libs = project.extensions.getByType<VersionCatalogsExtension>().named("libs") as org.gradle.accessors.dm.LibrariesForLibs
   dependencies{
      classpath (libs.kotlin.pluginGradle)
      classpath (libs.android.pluginGradle)
   }
}