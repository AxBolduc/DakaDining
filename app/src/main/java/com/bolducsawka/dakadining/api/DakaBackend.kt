package com.bolducsawka.dakadining.api

import com.bolducsawka.dakadining.api.requestobjects.*
import com.bolducsawka.dakadining.api.responseobjects.*
import com.bolducsawka.dakadining.dataobjects.Offer
import com.bolducsawka.dakadining.dataobjects.Request
import com.bolducsawka.dakadining.dataobjects.User
import retrofit2.Call
import retrofit2.http.*

interface DakaBackend {

    /**
     * Get user given a session id
     * @return User
     */
    @GET("/api/user/getUser/{sessionID}")
    fun getUserBySessionID(@Path("sessionID") sessionID: String): Call<ResponseObject<User>>

    /**
     * Logs the user in and responds with a LoginResponse
     * @return LoginResponse: {userid, sessionid}
     */
    @POST("/auth/login")
    fun loginUser(@Body credentials: LoginCredentials): Call<ResponseObject<LoginResponse>>

    /**
     * Creates a user in the database and responds with a Login Response
     * @return LoginResponse: {userid, sessionid}
     */
    @POST("/auth/createUser")
    fun createUser(@Body userInfo: CreateUserRequest): Call<ResponseObject<LoginResponse>>

    /**
     * Updates a user's meals in the database given a sessionID
     * @return UpdatePictureResponse {message}
     */
    @GET("/api/user/updateMeals/{sessionID}")
    fun updateMealsBySessionID(@Path("sessionID") sessionID: String, @Query("dir") direction: String, @Query("by") changeBy: Int): Call<ResponseObject<MealsUpdateReponse>>

    /**
     * Updates a user's profile picture in the database
     * @return UpdatePictureResponse
     */
    @POST("/api/user/updateProfilePicture")
    fun updateProfilePicture(@Body updatePictureRequest: UpdatePictureRequest): Call<ResponseObject<UpdatePictureResponse>>

    /**
     * Creates a new request in the database and return the created response
     * @return Response
     */
    @POST("/api/requests/newRequest")
    fun newRequest(@Body newRequest: Request): Call<ResponseObject<Request>>

    /**
     * Creates a new offer in the databse and return the created offer
     * @return Offer
     */
    @POST("/api/offers/newOffer")
    fun newOffer(@Body newOffer: Offer): Call<ResponseObject<Offer>>

    /**
     * Gets a list of all requests
     * @return GetRequestsResponse: {List<Requests>}
     */
    @GET("/api/requests/getRequests")
    fun getRequests(): Call<ResponseObject<GetRequestsResponse>>

    /**
     * Gets a list of all Offers
     * @return GetOffersResponse: {List<Requests>}
     */
    @GET("/api/offers/getOffers")
    fun getOffers(): Call<ResponseObject<GetOffersResponse>>

    /**
     * Fills a request in the databse
     * @return RequestFilledResponse
     */
    @POST("/api/requests/fillRequest")
    fun fillRequest(@Body fillRequest: FillRequestData): Call<ResponseObject<RequestFilledResponse>>

    /**
     * Takes an offer in the database
     * @return OfferTakenResponse
     */
    @POST("/api/offers/takeOffer")
    fun takeOffer(@Body takeOffer: TakeOfferData): Call<ResponseObject<OfferTakenResponse>>

    /**
     * Updates the full request object in the database
     * @return Updated Request
     */
    @POST("/api/requests/updateRequest")
    fun updateRequest(@Body updatedRequest: Request): Call<ResponseObject<Request>>

    /**
     * Deletes the given request from the database
     * @return Request deleted (only used for message)
     */
    @POST("/api/requests/deleteRequest")
    fun deleteRequest(@Body deleteRequest: Request): Call<ResponseObject<Request>>

    /**
     * Updates the fill offer in the database
     * @return Updated Request
     */
    @POST("/api/offers/updateOffer")
    fun updateOffer(@Body updatedOffer: Offer): Call<ResponseObject<Offer>>

    /**
     * Deletes the given request from the database
     * @return Offer deleted (used for message)
     */
    @POST("/api/offers/deleteOffer")
    fun deleteOffer(@Body deleteOffer: Offer): Call<ResponseObject<Offer>>



}