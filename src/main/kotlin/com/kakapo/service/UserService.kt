package com.kakapo.service

import com.kakapo.data.model.User
import com.kakapo.data.repository.user.UserRepository
import com.kakapo.data.request.CreateAccountRequest

class UserService(
    private val userRepository: UserRepository
) {

    suspend fun createUserAccount(request: CreateAccountRequest){
        userRepository.userSignUp(
            User(
                email = request.email,
                username = request.username,
                password = request.password,
                salt = "123456"
            )
        )
    }

    fun validateCreateAccountRequest(request: CreateAccountRequest): UserService.ValidationEvent{
        if (request.email.isBlank() || request.password.isBlank() || request.username.isBlank()){
            return ValidationEvent.ErrorFieldEmpty
        }
        return ValidationEvent.Success
    }

    sealed class ValidationEvent{
        object ErrorFieldEmpty: ValidationEvent()
        object Success: ValidationEvent()
    }
}