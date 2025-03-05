plugins {
    id("com.android.library")
    id("maven-publish")
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kapt)
}

val libMinSdk: Int by rootProject.extra
val libCompileSdk: Int by rootProject.extra
val libTargetSdk: Int by rootProject.extra
val javaVersion: JavaVersion by rootProject.extra
val libVersionName: String by rootProject.extra

android {
    namespace = "com.infomaniak.lib.pdfview"

    defaultConfig {
        minSdk = libMinSdk
        compileSdk = libCompileSdk
        targetSdk = libTargetSdk
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }

    compileOptions {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }

    kotlinOptions {
        jvmTarget = javaVersion.toString()
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.recyclerview)
    implementation(libs.viewpager2)

    api(libs.pdfium)
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components.findByName("release")!!)
                groupId = "com.github.Infomaniak"
                artifactId = "android-pdfview"
                version = libVersionName
            }
        }
    }
}
