package com.example

import com.example.plugins.configureRouting
import io.ktor.server.application.*


fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
//    install(ContentNegotiation) {
//        json(Json {
//            prettyPrint = true
//            isLenient = true
//        })
//    }
    configureRouting()
}


            //install(ContentNegotiation)
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



