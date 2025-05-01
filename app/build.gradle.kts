plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.pokemon_red_radical"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.pokemon_red_radical"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    //REFERENCIA: https://developer.android.com/develop/ui/views/layout/cardview?hl=es-419
    //BIBLIOTECA CARDVIEW PARA MOSTRAR LA INFORMACIÓN CON DISEÑO DE CARTA
    implementation(libs.androidx.cardview)

    //REFERENCIA:https://developer.android.com/training/wearables/apps/splash-screen
    //BIBLIOTECA PARA UTILIZAR SPLASHSCREEN E IMPLEMENTARLA EN LA APLICACIÓN
    implementation(libs.androidx.core.splashscreen.v100)

    //REFERENCIA: https://medium.com/@pritam.karmahapatra/retrofit-in-android-with-kotlin-9af9f66a54a8
    //BIBLIOTECA PARA UTILIZAR APIS REST, EN ESTE CASO LA DE POKEMON
    //UTILIZAR RETROFIT PARA REALIZAR SOLICITUDES HTTP Y EXTRAER LA INFORMACION DEL DATASET DE POKEMON A TRAVES DE LA POKEAPI
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    //CONVERTIR LOS DATOS JSON EXTRAIDOS DE LA API A FORMATO GSON CON LA FINALIDAD DE CONVERTIRLOS EN OBJETOS DE JAVA O KOTLIN
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    //REFERENCIA: https://github.com/bumptech/glide
    //GLIDE
    implementation ("com.github.bumptech.glide:glide:4.11.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.11.0")

    //BIBLIOTECAS POR DEFECTO
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

}