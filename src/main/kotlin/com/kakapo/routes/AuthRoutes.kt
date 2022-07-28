package com.kakapo.routes

import com.kakapo.data.request.CreateAccountRequest
import com.kakapo.data.request.LoginRequest
import com.kakapo.data.responses.AuthResponse
import com.kakapo.data.responses.BasicApiResponse
import com.kakapo.security.token.TokenConfig
import com.kakapo.service.UserService
import com.kakapo.utils.ApiResponseMessage
import com.kakapo.utils.Routes.CREATE_USER_ROUTE
import com.kakapo.utils.Routes.SIGN_IN_USER_ROUTE
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.createUser(
    userService: UserService
){
    post(CREATE_USER_ROUTE) {
        val request = call.receiveOrNull<CreateAccountRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val doesUserAlreadyExist = userService.doesUserWithEmailAlreadyExist(request.email)
        if (doesUserAlreadyExist){
            call.respond(
                BasicApiResponse<Unit>(
                    successful = false,
                    message = ApiResponseMessage.USER_ALREADY_EXISTS
                )
            )
            return@post
        }

        when(userService.validateCreateAccountRequest(request)){
            UserService.ValidationEvent.ErrorFieldEmpty -> {
                call.respond(
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = ApiResponseMessage.FIELDS_IS_BLANK
                    )
                )
            }
            UserService.ValidationEvent.Success -> {
                val wasAcknowledge = userService.createUserAccount(request)
                if (!wasAcknowledge){
                    call.respond(HttpStatusCode.Conflict)
                    return@post
                }
                call.respond(
                    BasicApiResponse<Unit>(successful = true)
                )
            }
        }
    }
}

fun Route.userSignIn(
    userService: UserService,
    tokenConfig: TokenConfig
){
    post(SIGN_IN_USER_ROUTE) {
        val request = call.receiveOrNull<LoginRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val validateLoginField = userService.validateLoginUserRequest(request)
        if (validateLoginField){
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val user = userService.getUserByEmail(request.email) ?: kotlin.run {
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse<Unit>(
                    successful = false,
                    message = ApiResponseMessage.USER_NOT_FOUND
                )
            )
            return@post
        }

        val isValidPassword = userService.isValidPassword(request.password, user)
        if (!isValidPassword){
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse<Unit>(
                    successful = false,
                    message = ApiResponseMessage.PASSWORD_INCORRECT
                )
            )
        }

        val token = userService.generateToken(user = user, tokenConfig = tokenConfig)

        call.respond(
            HttpStatusCode.OK,
            BasicApiResponse<AuthResponse>(
                successful = true,
                data = AuthResponse(token)
            )
        )
    }
}