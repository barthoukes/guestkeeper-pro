plugins {
   id("com.android.application")
   id("org.jetbrains.kotlin.android")
   id("kotlin-kapt")
   id("kotlin-parcelize")
}

android {
   namespace = "com.guestkeeper.pro"
   compileSdk = 34

   defaultConfig {
      applicationId = "com.guestkeeper.pro"
      minSdk = 24
      targetSdk = 34
      versionCode = 1
      versionName = "1.0.0"

      testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
      vectorDrawables {
         useSupportLibrary = true
      }
   }

   buildTypes {
      release {
         isMinifyEnabled = true
         isShrinkResources = true
         proguardFiles(
            getDefaultProguardFile("proguard-android-optimize.txt"),
            "proguard-rules.pro"
         )
      }
      debug {
         isDebuggable = true
      }
   }

   compileOptions {
      sourceCompatibility = JavaVersion.VERSION_17
      targetCompatibility = JavaVersion.VERSION_17
   }

   kotlinOptions {
      jvmTarget = "17"
   }

   buildFeatures {
      compose = false
      viewBinding = true
      dataBinding = false
   }

   packaging {
      resources {
         excludes += "/META-INF/{AL2.0,LGPL2.1}"
      }
   }
}

dependencies {
   // Core Android
   implementation("androidx.core:core-ktx:1.12.0")
   implementation("androidx.appcompat:appcompat:1.6.1")
   implementation("androidx.activity:activity-ktx:1.8.2")
   implementation("androidx.fragment:fragment-ktx:1.6.2")
   implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
   implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
   implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")

   // UI Components
   implementation("com.google.android.material:material:1.11.0")
   implementation("androidx.constraintlayout:constraintlayout:2.1.4")
   implementation("androidx.recyclerview:recyclerview:1.3.2")
   implementation("androidx.cardview:cardview:1.0.0")
   implementation("androidx.viewpager2:viewpager2:1.0.0")

   // Room Database
   implementation("androidx.room:room-runtime:2.6.1")
   implementation("androidx.room:room-ktx:2.6.1")
   kapt("androidx.room:room-compiler:2.6.1")

   // CameraX
   implementation("androidx.camera:camera-core:1.3.2")
   implementation("androidx.camera:camera-camera2:1.3.2")
   implementation("androidx.camera:camera-lifecycle:1.3.2")
   implementation("androidx.camera:camera-view:1.3.2")

   // Image Processing
   implementation("com.github.bumptech.glide:glide:4.16.0")
   implementation("id.zelory:compressor:3.0.1")

   // Security
   implementation("androidx.security:security-crypto:1.1.0-alpha06")
   implementation("net.zetetic:android-database-sqlcipher:4.5.4")

   // PDF Generation
   implementation("com.itextpdf:itext7-core:7.2.5")
   implementation("com.opencsv:opencsv:5.9")

   // Permissions
   implementation("com.github.permissions-dispatcher:permissionsdispatcher:4.9.2")
   kapt("com.github.permissions-dispatcher:permissionsdispatcher-processor:4.9.2")

   // WorkManager for notifications
   implementation("androidx.work:work-runtime-ktx:2.9.0")

   // Testing
   testImplementation("junit:junit:4.13.2")
   androidTestImplementation("androidx.test.ext:junit:1.1.5")
   androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
   testImplementation("androidx.room:room-testing:2.6.1")
}