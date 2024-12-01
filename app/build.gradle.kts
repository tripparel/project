plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android") // Kotlin Android 플러그인
}

android {
    namespace = "com.example.tripparel"
    compileSdk = 34 // 최신 Android 버전으로 업데이트

    defaultConfig {
        applicationId = "com.example.tripparel"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles("proguard-android.txt", "proguard-rules.pro")
        }
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
    implementation("androidx.appcompat:appcompat:1.7.0") // 최신 버전
    implementation("com.google.android.material:material:1.12.0") // 최신 버전
    implementation("androidx.activity:activity:1.9.3") // 최신 버전
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0") // 최신 버전
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1") // 최신 버전
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Tmap SDK
    implementation(files("libs/vsm-tmap-sdk-v2-android-1.7.23.aar"))
    implementation(files("libs/tmap-sdk-1.6.aar"))
}












