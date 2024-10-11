package com.example.plugins

import com.google.firebase.cloud.FirestoreClient
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


            log.info("Received POST /save-token with data: $request")

            if (token != null && userId != null) {
                val db = FirestoreClient.getFirestore()
                val docRef = db.collection("users").document(userId)

                val data = hashMapOf<String, Any>(
                    "token" to token
                )

                try {
                    docRef.set(data).get()
                    log.info("docRef.set(data).get()")
                } catch (e: Exception) {
                    log.info("Exception: $e")
                }

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
//    val data = hashMapOf<String, Any>( //val mapOf
//        "token" to token
//    )
//    try {
//        docRef.set(data).get()
//        println("Token successfully saved for user: $userId")
//    } catch (e: Exception) {
//        println("Error saving token: ${e.message}")
//    }
//}