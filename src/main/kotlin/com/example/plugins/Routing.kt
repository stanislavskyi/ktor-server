package com.example.plugins

import com.google.firebase.cloud.FirestoreClient
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
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



                log.info("TOKEN: ${request.token}")
                log.info("USERID: ${request.userId}")
                log.info("\n\n\n\n\n\n\n")

                val client = HttpClient(CIO) {
                    install(ContentNegotiation) {
                        gson()
                    }
                }

                sendNotification(client, request.token)
                log.info("SEND NOTIFICATION SUCCESSFULL")
                //saveTokenToDatabase(request.userId, request.token)

//                val client = HttpClient(CIO) {
//                    install(ContentNegotiation) {
//                        gson()
//                    }
//                }
//
//                val response: HttpResponse = client.post("https://fcm.googleapis.com/v1/projects/musicplayerapplication-be7c8/messages:send") {
//                    headers {
//                        append("Authorization", "Bearer ya29.a0AcM612xukjw9lJwVYG3AadUW-LzxOkyCjf_nte4ZMJrUT5Q6i1LJOTC9CUXX62vnCSSOaC5Ef23ryj80vD4v3yuXFgPSeVjg5zs1jipGHT07Qf9y9cazSRbbXNlxG1QtAiQKSbbHt0j6jRBsiZiEmdTqG23MM2m3gUdiFbo0aCgYKAQsSARMSFQHGX2MidvhmMejlauPJMa4v5cNITg0175")
//                        append("Content-Type", "application/json")
//                    }
//                    setBody(
//                        """
//            {
//              "message": {
//                "token": "${request.token}",
//                "notification": {
//                  "title": "title",
//                  "body": "body"
//                }
//              }
//            }
//            """.trimIndent()
//                    )
//                }
//
//                log.info("RESPONSE FCM: ${response.bodyAsText()}")
//                client.close()


            } catch (e: Exception) {
                log.info("ОШИБКА: ${e.message}")
            }
        }
    }
}


suspend fun sendNotification(client: HttpClient, token: String) {

    val response: HttpResponse =
        client.post("https://fcm.googleapis.com/v1/projects/musicplayerapplication-be7c8/messages:send") {
            headers {
                append(
                    "Authorization",
                    "Bearer ya29.a0AcM612xukjw9lJwVYG3AadUW-LzxOkyCjf_nte4ZMJrUT5Q6i1LJOTC9CUXX62vnCSSOaC5Ef23ryj80vD4v3yuXFgPSeVjg5zs1jipGHT07Qf9y9cazSRbbXNlxG1QtAiQKSbbHt0j6jRBsiZiEmdTqG23MM2m3gUdiFbo0aCgYKAQsSARMSFQHGX2MidvhmMejlauPJMa4v5cNITg0175"
                )
                append("Content-Type", "application/json")
            }
            setBody(
                """
            {
              "message": {
                "token": "$token",
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