package com.bolducsawka.dakadining.api.responseobjects

data class LoginResponse(
    val userID: String,
    val sessionID: String,
    val message: String
): ResponseData