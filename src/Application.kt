package me.joshuakaranja

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.auth.*
import com.fasterxml.jackson.databind.*
import io.ktor.auth.jwt.jwt
import io.ktor.jackson.*
import io.ktor.features.*
import io.ktor.request.receive

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(Authentication) {

    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        header("MyCustomHeader")
        allowCredentials = true
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }

    data class Translation(val swahili: String = "Mkahawa", val english: String = "Hotel")

    val Translations = mutableListOf(
        Translation(),
        Translation(swahili = "Nyumba",english = "House"),
        Translation(swahili = "Mtu", english = "Person"),
        Translation(swahili = "Gari", english = "Car")
    )

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        get("/json/jackson") {
            call.respond(mapOf("Tanslations" to synchronized(Translations){Translations}))
        }

        post("/translation") {
            //todo the data type is the problem
            val a = call.receive<Any>()
           // Translations += translation
            call.respond(mapOf("Ok" to a))
        }
    }
}

