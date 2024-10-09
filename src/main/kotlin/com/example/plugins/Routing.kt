package com.example.plugins

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
