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

            log.info("ВЫЗВАН МЕТОД POST!")

            val request = call.receive<Map<String, String>>()
            val token = request["token"]
            val userId = request["userId"]


            log.info("Получен POST/save-token с данными: $request")
            log.info("Получен token: $token")
            log.info("Получен userId: $userId")

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

                call.respond(HttpStatusCode.OK, "ТОКЕН СОХРАНЕН УСПЕШНО")
            } else {
                call.respond(HttpStatusCode.BadRequest, "ОТСУТСТВУЕТ ТОКЕН ИЛИ АЙДИ")
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