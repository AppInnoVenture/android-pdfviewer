buildscript {

    extra.apply {
        set("libMinSdk", 21)
        set("libCompileSdk", 36)
        set("libVersionName", "3.2.14")
        set("javaVersion", JavaVersion.VERSION_17)
        set("kotlinVersion", "2.0.21")
    }

    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(libs.agp)
    }
}

apply(plugin = "maven-publish")

allprojects {
    repositories {
        google()
        //mavenLocal()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
