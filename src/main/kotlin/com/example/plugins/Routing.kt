package com.example.plugins

import com.google.cloud.firestore.FirestoreOptions
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        post("/save-token") {
            val request = call.receive<Map<String, String>>()
            val token = request["token"]
            val userId = request["userId"]

            if (token != null && userId != null) {
                //saveTokenToDatabase(userId, token)
                call.respond(HttpStatusCode.OK, "Token saved successfully")
            } else {
                call.respond(HttpStatusCode.BadRequest, "Missing token or userId")
            }
        }
    }
}

//fun saveTokenToDatabase(userId: String, token: String) {
//    // Initialize Firestore
//    val firestore = FirestoreOptions.getDefaultInstance().service
//
//    // Create a reference to the user's document
//    val docRef = firestore.collection("users").document(userId)
//
//    // Create a map of data to update in the document
//    val data = hashMapOf<String, Any>(
//        "token" to token
//    )
//
//    // Update the document
//    docRef.set(data).addListener({
//        println("Token successfully saved for user: $userId")
//    }, {
//        println("Error saving token")
//    })
//}