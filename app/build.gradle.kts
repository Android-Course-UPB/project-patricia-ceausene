plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.ecotracker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.ecotracker"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    implementation("androidx.compose.ui:ui-text-google-fonts:1.6.7")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.2")
    testImplementation("org.mockito:mockito-core:3.11.2")
    testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")


    // Import the BoM for the Firebase platform
    implementation (platform("com.google.firebase:firebase-bom:32.0.0"))
    //authentication
    implementation("com.google.firebase:firebase-auth-ktx")
    //firestore
    implementation("com.google.firebase:firebase-firestore-ktx")
    //storage
    implementation("com.google.firebase:firebase-storage-ktx")
    // coroutine
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.1.1")
    //navigation
    implementation("androidx.navigation:navigation-compose:2.7.1")
    // viewmodel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")

    //Splash Api
    implementation("androidx.core:core-splashscreen:1.0.1")

    //profile pic
    implementation("io.coil-kt:coil-compose:1.3.2")

    //http requests
    implementation("com.squareup.okhttp3:okhttp:4.9.3")

    implementation("co.yml:ycharts:2.1.0")
}