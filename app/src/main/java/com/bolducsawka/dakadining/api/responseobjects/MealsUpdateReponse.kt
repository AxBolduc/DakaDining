package com.bolducsawka.dakadining.api.responseobjects

data class MealsUpdateReponse(
    val userID: String,
    val meals: Int,
    val message: String
): ResponseData