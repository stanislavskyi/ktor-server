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

        post("/save-token") {
            log.info("ВЫЗВАН МЕТОД POST!")

            try {
                val request = call.receive<TokenData>()

                log.info("TOKEN: ${request.token}")
                log.info("USERID: ${request.userId}")
                log.info("\n\n\n\n\n\n\n")

                val tokentoken = getTokenFromFirebaseDB(request.userId)
                log.info("\n\n\n\n\n\n\n$tokentoken")

                //sendNotification(request.token)
                log.info("SEND NOTIFICATION SUCCESSFULL")
                //saveTokenToDatabase(request.userId, request.token)


            } catch (e: Exception) {
                log.info("ОШИБКА: ${e.message}")
            }
        }
    }
}

//fun getAccessToken(): String {
//
//    val firebaseConfig = System.getenv("SERVICE_ACCOUNT_KEY")
//    log.info("SERVICE_ACCOUNT_KEY $firebaseConfig")
//
//
//    val googleCredentials = GoogleCredentials.fromStream(firebaseConfig.byteInputStream())
//        .createScoped(listOf("https://www.googleapis.com/auth/firebase.messaging"))
//    googleCredentials.refreshIfExpired()
//
//    return googleCredentials.accessToken.tokenValue
//}
//
//suspend fun sendNotification(token: String) {
//    val client = HttpClient(CIO)
//    val accessToken = getAccessToken()
//    log.info("ACCESS TOKEN $accessToken")
//    val response: HttpResponse =
//        client.post("https://fcm.googleapis.com/v1/projects/musicplayerapplication-be7c8/messages:send") {
//            headers {
//                append(
//                    "Authorization",
//                    "Bearer $accessToken"
//                )
//                append("Content-Type", "application/json")
//            }
//            setBody(
//                """
//            {
//              "message": {
//                "token": "eU1ayE9AS3ab1cC2t3FWH7:APA91bGG1IR_jPofgdA8MKaOs_bISyM96_i_Uut2L1F8SDGgPmhgt6XyoKSQvq1urDfUOahOT4SqbQP3MqxZi9yX-flbBZ-Tifex8x1kRAYzaK_zTSe-ws-FhrZDN6_bPkCsWeO_JjRM",
//                "notification": {
//                  "title": "title",
//                  "body": "body"
//                }
//              }
//            }
//            """.trimIndent()
//            )
//        }
//
//    log.info("RESPONSE FCM: ${response.bodyAsText()}")
//    client.close()
//}


fun getTokenFromFirebaseDB(userId: String): String? {
    val firestore: Firestore = FirestoreClient.getFirestore()
    val docRef = firestore.collection("users").document(userId)
    val future: ApiFuture<DocumentSnapshot> = docRef.get() // Асинхронный вызов

    return try {
        val document = future.get() // Ожидаем результата синхронно
        if (document.exists()) {
            document.getString("token") // Возвращаем FCM токен, если документ существует
        } else {
            null
        }
    } catch (e: Exception) {
        println("Ошибка получения токена: ${e.message}")
        null
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

    log.info("UPDATE DATA \n\n")

}