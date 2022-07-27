package com.kakapo.data.request

@kotlinx.serialization.Serializable
data class CreateAccountRequest(
    val email: String,
    val username: String,
    val password: String
)