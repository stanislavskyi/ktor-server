package com.example

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

object FirebaseAdmin {
    fun init() {
        val firebaseConfig = System.getenv("SERVICE_ACCOUNT_KEY") // переменная окружения с JSON конфигурацией
        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(firebaseConfig.byteInputStream()))
            .build()

        FirebaseApp.initializeApp(options)
    }
}