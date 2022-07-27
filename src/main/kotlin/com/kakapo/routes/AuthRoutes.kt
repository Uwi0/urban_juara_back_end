package com.kakapo.routes

import com.kakapo.data.request.CreateAccountRequest
import com.kakapo.data.responses.BasicApiResponse
import com.kakapo.security.hashing.HashingService
import com.kakapo.service.UserService
import com.kakapo.utils.ApiResponseMessage
import com.kakapo.utils.Routes.CREATE_USER_ROUTE
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.net.http.HttpResponse

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
                userService.createUserAccount(request)
                call.respond(
                    BasicApiResponse<Unit>(successful = true)
                )
            }
        }
    }
}