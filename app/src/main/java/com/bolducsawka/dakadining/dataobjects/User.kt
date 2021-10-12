package com.bolducsawka.dakadining.dataobjects

import com.bolducsawka.dakadining.api.responseobjects.ResponseData
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(
    @SerializedName("_id") val userID: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    var meals: Int,
    val plan: Int,
    val role: String,
    val session: String,
    val message: String?,
    var profilePic: String?):Serializable, ResponseData