plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
}

android {
    namespace = "tn.esprit.greenworld"
    compileSdk = 34

    defaultConfig {
        applicationId = "tn.esprit.greenworld"
        minSdk = 24
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
    buildFeatures{
        viewBinding = true
        dataBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

}

dependencies {

    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.room:room-common:2.6.0")
    implementation("androidx.room:room-ktx:2.6.0")
    implementation("com.google.android.gms:play-services-cast-tv:21.0.0")
    implementation("com.google.firebase:protolite-well-known-types:18.0.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    var nav_version = "2.7.5"
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")
    implementation("com.intuit.sdp:sdp-android:1.0.6")
    implementation("com.intuit.ssp:ssp-android:1.0.6")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.3.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.4.0")
    implementation("com.github.bumptech.glide:glide:4.12.0")
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.17")
    implementation("com.stripe:stripe-android:20.34.4")
    implementation("com.google.code.gson:gson:2.8.6")

    // Other dependencies...

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.squareup.okhttp3:okhttp:4.9.1")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.1")
    implementation ("com.squareup.picasso:picasso:2.71828")
    implementation ("com.cloudinary:cloudinary-android:2.5.0")
    //chaima
    val preference_version = "1.2.1"
    implementation ("com.google.android.gms:play-services-maps:17.0.1")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.preference:preference:1.2.1")
    implementation("androidx.preference:preference-ktx:$preference_version")
    implementation ("org.osmdroid:osmdroid-android:6.1.7")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.1")
    implementation ("com.facebook.android:facebook-android-sdk:+")
    implementation ("com.facebook.android:facebook-share:+")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation ("com.github.mindinventory:minavdrawer:1.2.2")
    implementation("io.github.muddz:styleabletoast:2.4.0")
    implementation ("com.fasterxml.jackson.core:jackson-core:2.13.0")
    implementation ("com.fasterxml.jackson.core:jackson-databind:2.13.0")
    implementation ("com.applandeo:material-calendar-view:1.9.0-rc03")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")



    implementation ("com.github.dhaval2404:imagepicker:1.7")

}

