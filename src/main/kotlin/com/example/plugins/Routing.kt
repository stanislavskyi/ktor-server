package com.example.plugins

import com.google.cloud.firestore.FirestoreOptions
import com.google.firebase.cloud.FirestoreClient
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

private val log: Logger = LoggerFactory.getLogger("MyPlugin")

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

                log.info("\n\n\n\n")

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

    log.info("TOKEN: ${token}")
    log.info("USERID: ${userId}")

    val firestore = FirestoreClient.getFirestore()
    log.info("FIRESTORE: $firestore")


    val docRef = firestore.collection("users").document(userId)
    log.info("DOCREF: $docRef")


    val data = hashMapOf<String, Any>( //val mapOf
        "token" to token
    )
    log.info("DATA MAP : $data")

    docRef.update(data).get()

}