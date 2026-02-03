plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.xiaohe.hyperclick"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.xiaohe.hyperclick"
        minSdk = 35
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    compileOnly("de.robv.android.xposed:api:82")
}
android {
    packaging {
        jniLibs {
            useLegacyPackaging = true
        }
    }
}
