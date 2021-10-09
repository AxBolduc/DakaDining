package com.bolducsawka.dakadining.api.responseobjects

data class ResponseObject<T: ResponseData>(val status: Int, val data: T)