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
            log.info("ВЫЗВАН МЕТОД POST!")

            try {
                val request = call.receive<TokenData>()
                log.info("Получен POST /save-token REQUEST с данными: ${request.token}, ${request.userId}")
                call.respondText("Данные успешно сохранены")

                saveTokenToDatabase(request.userId, request.token)

                call.respondText("ВСЁ ПОЛУЧИЛОСЬ")

            } catch (e: Exception) {
                log.info("ОШИБКА: ${e.message}")
                call.respondText("Ошибка при обработке данных: ${e.message}", status = HttpStatusCode.BadRequest)
            }
        }
    }
}

fun saveTokenToDatabase(userId: String, token: String) {
    // Initialize Firestore
    val firestore = FirestoreOptions.getDefaultInstance().service

    // Create a reference to the user's document
    val docRef = firestore.collection("users").document(userId)

    // Create a map of data to update in the document
    val data = hashMapOf<String, Any>( //val mapOf
        "token" to token
    )

    docRef.set(data).get()

}