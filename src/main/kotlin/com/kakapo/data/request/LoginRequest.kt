package com.kakapo.data.request

@kotlinx.serialization.Serializable
data class LoginRequest(
    val email: String,
    val password: String
)
