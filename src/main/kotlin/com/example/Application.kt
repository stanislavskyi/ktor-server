package com.example

import com.example.plugins.configureRouting
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.ktor.server.application.*
import java.io.ByteArrayInputStream

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    val serviceAccountJson = System.getenv("SERVICE_ACCOUNT_JSON")

    //val serviceAccount = FileInputStream("google-services.json")

    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(ByteArrayInputStream(serviceAccountJson.toByteArray())))
        //.setDatabaseUrl("https://musicplayerapplication-be7c8.firebaseio.com")
        .build()

    FirebaseApp.initializeApp(options)
    configureRouting()
}
