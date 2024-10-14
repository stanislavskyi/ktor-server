package com.example.plugins

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.cloud.FirestoreClient
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.FileInputStream


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

                log.info("TOKEN: ${request.token}")
                log.info("USERID: ${request.userId}")
                log.info("\n\n\n\n\n\n\n")

                val client = HttpClient(CIO)

                sendNotification(client, request.token)
                log.info("SEND NOTIFICATION SUCCESSFULL")
                //saveTokenToDatabase(request.userId, request.token)


            } catch (e: Exception) {
                log.info("ОШИБКА: ${e.message}")
            }
        }
    }
}

fun getAccessToken(): String {

    val firebaseConfig = System.getenv("SERVICE_ACCOUNT_KEY")
    log.info("SERVICE_ACCOUNT_KEY $firebaseConfig")

    val googleCredentials = GoogleCredentials.fromStream(firebaseConfig.byteInputStream())
        .createScoped(listOf("https://www.googleapis.com/auth/firebase.messaging"))
    googleCredentials.refreshIfExpired()

    return googleCredentials.accessToken.tokenValue
}

suspend fun sendNotification(client: HttpClient, token: String) {

    val accessToken = getAccessToken()
    log.info("ACCESS TOKEN $accessToken")
    val response: HttpResponse =
        client.post("https://fcm.googleapis.com/v1/projects/musicplayerapplication-be7c8/messages:send") {
            headers {
                append(
                    "Authorization",
                    "Bearer $accessToken"
                )
                append("Content-Type", "application/json")
            }
            setBody(
                """
            {
              "message": {
                "token": "fYuluxZMSjS4ZBPIFtrPy2:APA91bFqdl4qC-ai8c04AY3CkTJNLVjDNvIKISqKwjmdLw3zOBagP-tIM-9EvTi-fsRAWd7F0gRjki2mbHsLAYkFr90U7yl4PYQZkDeIYug6jbycgZTfX2s1ILaqZEzl0GuAowDtxU2U",
                "notification": {
                  "title": "title",
                  "body": "body"
                }
              }
            }
            """.trimIndent()
            )
        }

    log.info("RESPONSE FCM: ${response.bodyAsText()}")
    client.close()
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

    log.info("UPDATE DATA \n\n")

}