package com.example

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

object FirebaseAdmin {
    fun init() {
        val firebaseConfig = System.getenv("SERVICE_ACCOUNT_KEY") // переменная окружения с JSON конфигурацией
        val serviceAccountKey = this::class.java.classLoader.getResourceAsStream("musicplayerapplication-be7c8-firebase-adminsdk-stp2f-e1126abb8f.json")

        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(firebaseConfig.byteInputStream())) //firebaseConfig.byteInputStream()
            .build()

        FirebaseApp.initializeApp(options)
    }
}
