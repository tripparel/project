buildscript {
    repositories {
        google()
        mavenCentral()
        flatDir { dirs("app/libs") } // libs 경로 수정
    }

    dependencies {
        classpath("com.android.tools.build:gradle:8.6.0") // 최신 버전
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.22") // 최신 버전
    }
}











