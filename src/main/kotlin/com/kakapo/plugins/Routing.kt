package com.kakapo.plugins

import com.kakapo.routes.createUser
import com.kakapo.routes.userSignIn
import com.kakapo.security.hashing.SHA256HashingService
import com.kakapo.security.token.TokenConfig
import com.kakapo.service.UserService
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.locations.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting(token: TokenConfig) {
    val userService: UserService by inject()

    routing {
        createUser(userService = userService)
        userSignIn(userService, token)
    }
}
