package com.example

import com.example.plugins.configureRouting
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import java.io.ByteArrayInputStream

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }

    val serviceAccountJson = System.getenv("SERVICE_ACCOUNT_KEY")
    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(ByteArrayInputStream(serviceAccountJson.toByteArray())))
        .build()

    FirebaseApp.initializeApp(options)

    configureRouting()
}



//    val serviceAccountJson = System.getenv("SERVICE_ACCOUNT_KEY")
//    println("serviceAccountJson: $serviceAccountJson")

//    println("asjfgasjgjasg")
//    //val serviceAccount = FileInputStream("google-services.json")
//
//
//    val fixedServiceAccountJson = serviceAccountJson.replace("\\n", "\n")
//    val options = FirebaseOptions.builder()
//        .setCredentials(GoogleCredentials.fromStream(ByteArrayInputStream(fixedServiceAccountJson.toByteArray())))
//        .build()
//
//    FirebaseApp.initializeApp(options)



