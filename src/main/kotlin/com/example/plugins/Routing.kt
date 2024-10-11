package com.example.plugins

import com.google.cloud.firestore.FirestoreOptions
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

            try {
                val request = call.receive<TokenData>()
                log.info("Получен POST /save-token REQUEST с данными: ${request.token}, ${request.userId}")


                val firebaseConfig = System.getenv("SERVICE_ACCOUNT_KEY")
                log.info("SERVICE_ACCOUNT_KEY $firebaseConfig")

                log.info("TOKEN: ${request.token}")
                log.info("USERID: ${request.userId}")

                saveTokenToDatabase(request.userId, request.token)



            } catch (e: Exception) {
                log.info("ОШИБКА: ${e.message}")
            }
        }
    }
}

fun saveTokenToDatabase(userId: String, token: String) {
    // Initialize Firestore
    //val firestore = FirestoreOptions.getDefaultInstance().service
    val firestore = FirestoreClient.getFirestore()

    // Create a reference to the user's document
    val docRef = firestore.collection("users").document(userId)

    // Create a map of data to update in the document
    val data = hashMapOf<String, Any>( //val mapOf
        "token" to token
    )

    docRef.set(data).get()

}