package com.bolducsawka.dakadining.api.responseobjects

import com.bolducsawka.dakadining.dataobjects.Offer

data class GetOffersResponse(
    val offers: List<Offer>
):ResponseData