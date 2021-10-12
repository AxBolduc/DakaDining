package com.bolducsawka.dakadining.dataobjects

import com.bolducsawka.dakadining.api.responseobjects.ResponseData

data class Offer(
    var offerer: String,
    var meals: Int,
    var price: Int,
    var status: Boolean,
    var takenBy: String?): ResponseData, SwipeObject