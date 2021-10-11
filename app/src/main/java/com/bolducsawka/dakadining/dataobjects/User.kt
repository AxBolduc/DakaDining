package com.bolducsawka.dakadining.dataobjects

import com.bolducsawka.dakadining.api.responseobjects.ResponseData
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import org.bson.types.ObjectId
import java.io.Serializable
import java.lang.IllegalArgumentException



/*
{
    "_id": "6160b560bef93f7dc49553e8",
    "firstName": "Alex",
    "lastName": "Bolduc",
    "email": "aebolduc@wpi.edu",
    "password": "$2b$10$40X2OsUooP.ZfjmlsBjBHuS4lyD6XU2OQwgRSo5fQXgL6FGKDV5jK",
    "meals": 19,
    "__v": 0
}

 */

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