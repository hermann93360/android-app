plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.example.dacontrolagent"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.dacontrolagent"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.github.blackfizz:eazegraph:1.2.5l@aar")
    implementation("com.nineoldandroids:library:2.4.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation("com.google.firebase:firebase-firestore:24.0.1")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")
    implementation(libs.play.services.maps)
    implementation(libs.firebase.database)
    annotationProcessor("androidx.lifecycle:lifecycle-compiler:2.3.1")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}