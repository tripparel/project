pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        flatDir { dirs("app/libs") } // libs 경로 수정
    }
    plugins {
        id("com.android.application") version "8.6.0" apply false
        id("org.jetbrains.kotlin.android") version "1.8.22" apply false // 최신 Kotlin 플러그인
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        flatDir { dirs("app/libs") } // libs 경로 수정
    }
}

rootProject.name = "tripparel"
include(":app")




