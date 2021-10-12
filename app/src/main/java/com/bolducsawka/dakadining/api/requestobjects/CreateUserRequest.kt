package com.bolducsawka.dakadining.api.requestobjects

data class CreateUserRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val meals: Int,
    val plan: Int,
    val role: String
)