package com.kakapo.data.repository.user

import com.kakapo.data.model.User
import org.litote.kmongo.coroutine.CoroutineDatabase

class MongoUserRepository(db: CoroutineDatabase): UserRepository {

    private val users = db.getCollection<User>()

    override suspend fun userSignUp(user: User) : Boolean{
        return users.insertOne(user).wasAcknowledged()
    }

}