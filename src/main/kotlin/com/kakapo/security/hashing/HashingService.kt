package com.kakapo.security.hashing

interface HashingService {
    fun generatedSaltedHash(value: String, saltLength: Int = 32): SaltedHash
    fun verifySalt(value: String, saltedHash: SaltedHash): Boolean
}