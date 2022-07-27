package com.kakapo.service

import com.kakapo.data.model.User
import com.kakapo.data.repository.user.UserRepository
import com.kakapo.data.request.CreateAccountRequest
import com.kakapo.security.hashing.HashingService

class UserService(
    private val userRepository: UserRepository,
    private val hashingService: HashingService
) {

    suspend fun doesUserWithEmailAlreadyExist(email: String): Boolean{
        return userRepository.getUserByEmail(email) != null
    }

    suspend fun createUserAccount(request: CreateAccountRequest){
        val saltedHash = hashingService.generatedSaltedHash(request.password)
        userRepository.userSignUp(
            User(
                email = request.email,
                username = request.username,
                password = saltedHash.hash,
                salt = saltedHash.salt
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