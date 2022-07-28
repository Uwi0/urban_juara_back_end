package com.kakapo

import com.kakapo.di.mainModule
import io.ktor.server.application.*
import com.kakapo.plugins.*
import com.kakapo.security.token.TokenConfig
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    install(Koin){
        modules(mainModule)
    }
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 365L * 1000L * 60L * 60L * 24L,
        secret = System.getenv("JWT_SECRET")
    )
    configureSecurity(tokenConfig)
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureRouting(tokenConfig)
}
