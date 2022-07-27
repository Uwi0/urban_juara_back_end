package com.kakapo.utils

object Constants {

    const val DATABASE_NAME = "urban_juara_server"

}

object Routes{
    private const val END_POINTS = "api"
    private const val USER_ROUTE = "$END_POINTS/user"
    const val CREATE_USER_ROUTE = "$USER_ROUTE/create"
}

object ApiResponseMessage{
    const val USER_ALREADY_EXISTS = "A User with email already exists."
    const val FIELDS_IS_BLANK = "The fields cannot be empty"
}