package com.bolducsawka.dakadining.api.responseobjects

import com.bolducsawka.dakadining.dataobjects.Request

data class GetRequestsResponse(
    val requests: List<Request>
):ResponseData