package com.kakapo.service

import com.kakapo.data.model.User
import com.kakapo.data.repository.user.UserRepository
import com.kakapo.data.request.CreateAccountRequest
import com.kakapo.data.request.LoginRequest
import com.kakapo.security.hashing.HashingService
import com.kakapo.security.hashing.SaltedHash
import com.kakapo.security.token.TokenClaim
import com.kakapo.security.token.TokenConfig
import com.kakapo.security.token.TokenService

class UserService(
    private val userRepository: UserRepository,
    private val hashingService: HashingService,
    private val tokenService: TokenService
) {

    suspend fun doesUserWithEmailAlreadyExist(email: String): Boolean{
        return userRepository.getUserByEmail(email) != null
    }

    suspend fun getUserByEmail(email: String): User?{
        return userRepository.getUserByEmail(email)
    }

    suspend fun createUserAccount(request: CreateAccountRequest): Boolean{
        val saltedHash = hashingService.generatedSaltedHash(request.password)
        return userRepository.userSignUp(
            User(
                email = request.email,
                username = request.username,
                password = saltedHash.hash,
                salt = saltedHash.salt
            )
        )
    }

    fun generateToken(tokenConfig: TokenConfig, user: User): String{
        return tokenService.generateToken(
            config = tokenConfig,
            TokenClaim(
                name = "userId",
                value = user.id
            )
        )
    }

    fun isValidPassword(value: String, user: User): Boolean{
        return hashingService.verifySalt(
            value = value,
            saltedHash = SaltedHash(
                hash = user.password,
                salt = user.salt
            )
        )
    }

    fun validateCreateAccountRequest(request: CreateAccountRequest): UserService.ValidationEvent{
        if (request.email.isBlank() || request.password.isBlank() || request.username.isBlank()){
            return ValidationEvent.ErrorFieldEmpty
        }
        return ValidationEvent.Success
    }

    fun validateLoginUserRequest(request: LoginRequest): Boolean{
        return request.email.isBlank() || request.password.isBlank()
    }

    sealed class ValidationEvent{
        object ErrorFieldEmpty: ValidationEvent()
        object Success: ValidationEvent()
    }
}