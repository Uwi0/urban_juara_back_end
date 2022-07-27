package com.kakapo.data.repository.user

import com.kakapo.data.model.User
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class MongoUserRepository(db: CoroutineDatabase): UserRepository {

    private val users = db.getCollection<User>()

    override suspend fun userSignUp(user: User) : Boolean{
        return users.insertOne(user).wasAcknowledged()
    }

    override suspend fun getUserByEmail(email: String): User? {
        return users.findOne(User::email eq email)
    }
}