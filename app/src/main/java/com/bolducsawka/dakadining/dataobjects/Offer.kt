package com.bolducsawka.dakadining.dataobjects

import com.bolducsawka.dakadining.api.responseobjects.ResponseData
import com.google.gson.annotations.SerializedName

data class Offer(
    @SerializedName("_id") val offerID: String,
    var offerer: String,
    var meals: Int,
    var price: Int,
    var status: Boolean,
    var takenBy: String?,
    var message: String?): ResponseData, SwipeObject