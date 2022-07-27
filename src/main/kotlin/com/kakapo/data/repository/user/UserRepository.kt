package com.kakapo.data.repository.user

import com.kakapo.data.model.User

interface UserRepository {
    suspend fun userSignUp(user: User): Boolean

    suspend fun getUserByEmail(email: String): User?
}