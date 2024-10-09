package com.example

import com.example.plugins.*
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.ktor.server.application.*
import java.io.FileInputStream

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    val serviceAccount = FileInputStream("google-services.json")

    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .setDatabaseUrl("https://musicplayerapplication-be7c8.firebaseio.com")
        .build()

    FirebaseApp.initializeApp(options)

    configureRouting()
}
