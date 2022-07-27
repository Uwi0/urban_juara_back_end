package com.kakapo.di

import com.kakapo.data.repository.user.MongoUserRepository
import com.kakapo.data.repository.user.UserRepository
import com.kakapo.service.UserService
import com.kakapo.utils.Constants
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val mainModule = module {
    single {
        val client = KMongo.createClient().coroutine
        client.getDatabase(Constants.DATABASE_NAME)
    }
    single<UserRepository> {
        MongoUserRepository(get())
    }
    single { UserService(get()) }
}