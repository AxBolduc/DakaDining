package com.bolducsawka.dakadining.api

import com.bolducsawka.dakadining.api.requestobjects.*
import com.bolducsawka.dakadining.api.responseobjects.*
import com.bolducsawka.dakadining.dataobjects.Offer
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

    @POST("/api/offers/newOffer")
    fun newOffer(@Body newOffer: Offer): Call<ResponseObject<Offer>>

    @GET("/api/requests/getRequests")
    fun getRequests(): Call<ResponseObject<GetRequestsResponse>>

    @GET("/api/offers/getOffers")
    fun getOffers(): Call<ResponseObject<GetOffersResponse>>

    @POST("/api/requests/fillRequest")
    fun fillRequest(@Body fillRequest: FillRequestData): Call<ResponseObject<RequestFilledResponse>>

    @POST("/api/offers/takeOffer")
    fun takeOffer(@Body takeOffer: TakeOfferData): Call<ResponseObject<OfferTakenResponse>>

    @POST("/api/requests/updateRequest")
    fun updateRequest(@Body updatedRequest: Request): Call<ResponseObject<Request>>

    @POST("/api/requests/deleteRequest")
    fun deleteRequest(@Body deleteRequest: Request): Call<ResponseObject<Request>>

}