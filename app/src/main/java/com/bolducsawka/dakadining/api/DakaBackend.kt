package com.bolducsawka.dakadining.api

import com.bolducsawka.dakadining.api.requestobjects.CreateUserRequest
import com.bolducsawka.dakadining.api.requestobjects.LoginCredentials
import com.bolducsawka.dakadining.api.responseobjects.LoginResponse
import com.bolducsawka.dakadining.api.responseobjects.ResponseObject
import com.bolducsawka.dakadining.dataobjects.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface DakaBackend {

    @GET("/api/user/getUser/{sessionID}")
    fun getUserBySessionID(@Path("sessionID") sessionID: String): Call<ResponseObject<User>>

    @POST("/auth/login")
    fun loginUser(@Body credentials: LoginCredentials): Call<ResponseObject<LoginResponse>>

    @POST("/auth/createUser")
    fun createUser(@Body userInfo: CreateUserRequest): Call<ResponseObject<LoginResponse>>

}