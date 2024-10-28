package com.example.plugins

import com.google.api.core.ApiFuture
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.DocumentSnapshot
import com.google.cloud.firestore.Firestore
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

        post("/save-userId") {
            try {
                val request = call.receive<TokenData>()
                val token = getTokenFromFirebaseDB(request.userId, request.currentUser)
                log.info("\nTOKEN /save-userId $token\n")

                if (token != null){
                    val tokenData = token["token"] ?: "Токен не найден"
                    val name = token["name"] ?: "Имя не найдено"

                    sendNotification(tokenData, name)
                }



            } catch (e: Exception) {
                log.info("ERROR /save-userId: ${e.message}")
            }
        }

        post("/save-token") {
            try {
                val request = call.receive<TokenData>()
                log.info("\n/save-token ${request.userId}, ${request.token}\n")
                saveTokenToDatabase(request.userId, request.token)
            } catch (e: Exception) {
                log.info("ERROR /save-token: ${e.message}")
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

suspend fun sendNotification(token: String, name: String) {
    val client = HttpClient(CIO)
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
                "token": "$token",
                "notification": {
                  "title": "${name.replace("@gmail.com", "")}",
                  "body": "Присоединиться к другу?"
                }
              }
            }
            """.trimIndent()
            )
        }

    log.info("RESPONSE FCM: ${response.bodyAsText()}")
    client.close()
}


fun getTokenFromFirebaseDB(userId: String, currentUser: String): Map<String, String>? {
    val firestore: Firestore = FirestoreClient.getFirestore()
    val docRef = firestore.collection("users").document(userId)
    val future: ApiFuture<DocumentSnapshot> = docRef.get() // Асинхронный вызов

    val currentDocRef = firestore.collection("users").document(currentUser)
    val currentFuture: ApiFuture<DocumentSnapshot> = currentDocRef.get() // Асинхронный вызов

    val result = mutableMapOf<String, String>()

    return try {
        val document = future.get() // Ожидаем результата синхронно
        val currentDocument = currentFuture.get()

        if (document.exists()) {
            val gt = document.getString("token")

            log.info("getTokenFromFirebaseDB \n\n$userId\n$gt\n\n")

            result["token"] = gt ?: "" // Добавляем токен, или пустую строку, если он равен null


        }

        if (currentDocument.exists()){
            val gt = currentDocument.getString("name")

            log.info("getTokenFromFirebaseDB \n\n$currentUser\n$gt\n\n")

            result["name"] = gt ?: "" // Добавляем токен, или пустую строку, если он равен null


        }

        return result

    } catch (e: Exception) {
        log.info("Ошибка получения токена: ${e.message}")
        null
    }
}

fun saveTokenToDatabase(userId: String, token: String) {

    val firestore = FirestoreClient.getFirestore()
    val docRef = firestore.collection("users").document(userId)

    val data = hashMapOf<String, Any>( //val mapOf
        "token" to token
    )

    docRef.update(data).get()
    log.info("saveTokenToDatabase")

}