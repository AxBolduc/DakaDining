package com.bolducsawka.dakadining.api

import com.bolducsawka.dakadining.api.requestobjects.CreateUserRequest
import com.bolducsawka.dakadining.api.requestobjects.LoginCredentials
import com.bolducsawka.dakadining.api.requestobjects.UpdatePictureRequest
import com.bolducsawka.dakadining.api.responseobjects.LoginResponse
import com.bolducsawka.dakadining.api.responseobjects.MealsUpdateReponse
import com.bolducsawka.dakadining.api.responseobjects.ResponseObject
import com.bolducsawka.dakadining.api.responseobjects.UpdatePictureResponse
import com.bolducsawka.dakadining.dataobjects.Request
import com.bolducsawka.dakadining.dataobjects.User
import retrofit2.Call
import retrofit2.http.*

interface DakaBackend {

    @GET("/api/user/getUser/{sessionID}")
    fun getUserBySessionID(@Path("sessionID") sessionID: String): Call<ResponseObject<User>>

    @POST("/auth/login")
    fun loginUser(@Body credentials: LoginCredentials): Call<ResponseObject<LoginResponse>>

    @POST("/auth/createUser")
    fun createUser(@Body userInfo: CreateUserRequest): Call<ResponseObject<LoginResponse>>

    @GET("/api/user/updateMeals/{sessionID}")
    fun updateMealsBySessionID(@Path("sessionID") sessionID: String, @Query("dir") direction: String, @Query("by") changeBy: Int): Call<ResponseObject<MealsUpdateReponse>>

    @POST("/api/user/updateProfilePicture")
    fun updateProfilePicture(@Body updatePictureRequest: UpdatePictureRequest): Call<ResponseObject<UpdatePictureResponse>>

    @POST("/api/requests/newRequest")
    fun newRequest(@Body newRequest: Request): Call<ResponseObject<Request>>

}