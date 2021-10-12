package com.bolducsawka.dakadining.dataobjects

import com.bolducsawka.dakadining.api.responseobjects.ResponseData
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class Request(
    @SerializedName("_id") var requestID: String,
    var requester: String,
    var meals: Int,
    var price: Int,
    var time: Date,
    val status: Boolean,
    val filledBy: String?,
    val message: String?):Serializable, ResponseData,SwipeObject
