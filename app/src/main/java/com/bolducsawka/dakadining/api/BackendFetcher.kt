package com.bolducsawka.dakadining.api

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bolducsawka.dakadining.api.requestobjects.*
import com.bolducsawka.dakadining.api.responseobjects.*
import com.bolducsawka.dakadining.dataobjects.Offer
import com.bolducsawka.dakadining.dataobjects.Request
import com.bolducsawka.dakadining.dataobjects.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.IllegalStateException

private const val TAG = "BackendFetcher"

class BackendFetcher private constructor(context: Context){
    private val dakaBackend: DakaBackend

    init{
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.221:4000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        dakaBackend = retrofit.create(DakaBackend::class.java)
    }

    fun getUserBySessionID(sessionID: String): LiveData<ResponseObject<User>>{
        val responseLiveData: MutableLiveData<ResponseObject<User>> = MutableLiveData()
        val backendRequest: Call<ResponseObject<User>> = dakaBackend.getUserBySessionID(sessionID)

        backendRequest.enqueue(object: Callback<ResponseObject<User>>{
            override fun onResponse(call: Call<ResponseObject<User>>, response: Response<ResponseObject<User>>) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(call: Call<ResponseObject<User>>, t: Throwable) {
                Log.d(TAG, "getUserBySessionID request failed")
            }
        })

        return responseLiveData
    }

    fun loginUser(credentials: LoginCredentials): LiveData<ResponseObject<LoginResponse>>{
        val responseLiveData: MutableLiveData<ResponseObject<LoginResponse>> = MutableLiveData()
        val backendRequest: Call<ResponseObject<LoginResponse>> = dakaBackend.loginUser(credentials)

        backendRequest.enqueue(object: Callback<ResponseObject<LoginResponse>>{
            override fun onFailure(call: Call<ResponseObject<LoginResponse>>, t: Throwable) {
                Log.d(TAG, "loginUser request failed")
            }

            override fun onResponse(call: Call<ResponseObject<LoginResponse>>, response: Response<ResponseObject<LoginResponse>>) {
                responseLiveData.value = response.body()
            }
        })

        return responseLiveData

    }

    fun createUser(userInfo: CreateUserRequest): LiveData<ResponseObject<LoginResponse>>{
        val responseLiveData: MutableLiveData<ResponseObject<LoginResponse>> = MutableLiveData()
        val backendRequest: Call<ResponseObject<LoginResponse>> = dakaBackend.createUser(userInfo)

        backendRequest.enqueue(object : Callback<ResponseObject<LoginResponse>>{
            override fun onResponse(
                call: Call<ResponseObject<LoginResponse>>,
                response: Response<ResponseObject<LoginResponse>>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(call: Call<ResponseObject<LoginResponse>>, t: Throwable) {
                Log.d(TAG, "createUser request failed")
                t.printStackTrace()
            }
        })

        return responseLiveData
    }

    fun updateMealsBySessionID(sessionID: String, isIncreasing: Boolean, changeBy: Int): LiveData<ResponseObject<MealsUpdateReponse>>{
        var direction = "dec"

        if(isIncreasing){
            direction = "inc"
        }

        val responseLiveData: MutableLiveData<ResponseObject<MealsUpdateReponse>> = MutableLiveData()
        val backendRequest: Call<ResponseObject<MealsUpdateReponse>> = dakaBackend.updateMealsBySessionID(sessionID, direction, changeBy)

        backendRequest.enqueue(object : Callback<ResponseObject<MealsUpdateReponse>>{
            override fun onResponse(
                call: Call<ResponseObject<MealsUpdateReponse>>,
                response: Response<ResponseObject<MealsUpdateReponse>>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(call: Call<ResponseObject<MealsUpdateReponse>>, t: Throwable) {
                Log.d(TAG, "UpdateMealsBySessionID Failed")
            }

        })

        return responseLiveData
    }

    fun updateProfilePicture(updatePictureRequest: UpdatePictureRequest): LiveData<ResponseObject<UpdatePictureResponse>>{
        val responseLiveData: MutableLiveData<ResponseObject<UpdatePictureResponse>> = MutableLiveData()
        val backendRequest: Call<ResponseObject<UpdatePictureResponse>> = dakaBackend.updateProfilePicture(updatePictureRequest)

        backendRequest.enqueue(object : Callback<ResponseObject<UpdatePictureResponse>>{
            override fun onResponse(
                call: Call<ResponseObject<UpdatePictureResponse>>,
                response: Response<ResponseObject<UpdatePictureResponse>>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<ResponseObject<UpdatePictureResponse>>,
                t: Throwable
            ) {
                Log.d(TAG, "updateProfilePicture failed")
                t.printStackTrace()
            }

        })

        return responseLiveData
    }

    fun newRequest(newRequest: Request): LiveData<ResponseObject<Request>>{
        val responseLiveData: MutableLiveData<ResponseObject<Request>> = MutableLiveData()
        val backendRequest: Call<ResponseObject<Request>> = dakaBackend.newRequest(newRequest)

        backendRequest.enqueue(object : Callback<ResponseObject<Request>> {
            override fun onResponse(
                call: Call<ResponseObject<Request>>,
                response: Response<ResponseObject<Request>>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(call: Call<ResponseObject<Request>>, t: Throwable) {
                t.printStackTrace()
            }
        })

        return responseLiveData
    }

    fun newOffer(newOffer: Offer): LiveData<ResponseObject<Offer>>{
        val responseLiveData: MutableLiveData<ResponseObject<Offer>> = MutableLiveData()
        val backendRequest: Call<ResponseObject<Offer>> = dakaBackend.newOffer(newOffer)

        backendRequest.enqueue(object : Callback<ResponseObject<Offer>> {
            override fun onResponse(
                call: Call<ResponseObject<Offer>>,
                response: Response<ResponseObject<Offer>>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(call: Call<ResponseObject<Offer>>, t: Throwable) {
                t.printStackTrace()
            }

        })

        return responseLiveData
    }

    fun getRequests(): LiveData<ResponseObject<GetRequestsResponse>>{
        val responseLiveData: MutableLiveData<ResponseObject<GetRequestsResponse>> = MutableLiveData()
        val backendRequest: Call<ResponseObject<GetRequestsResponse>> = dakaBackend.getRequests()

        backendRequest.enqueue(object : Callback<ResponseObject<GetRequestsResponse>>{
            override fun onResponse(
                call: Call<ResponseObject<GetRequestsResponse>>,
                response: Response<ResponseObject<GetRequestsResponse>>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(call: Call<ResponseObject<GetRequestsResponse>>, t: Throwable) {
                t.printStackTrace()
            }
        })

        return responseLiveData
    }

    fun getOffers(): LiveData<ResponseObject<GetOffersResponse>>{
        val responseLiveData: MutableLiveData<ResponseObject<GetOffersResponse>> = MutableLiveData()
        val backendRequest: Call<ResponseObject<GetOffersResponse>> = dakaBackend.getOffers()

        backendRequest.enqueue(object : Callback<ResponseObject<GetOffersResponse>>{
            override fun onResponse(
                call: Call<ResponseObject<GetOffersResponse>>,
                response: Response<ResponseObject<GetOffersResponse>>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(call: Call<ResponseObject<GetOffersResponse>>, t: Throwable) {
                t.printStackTrace()
            }
        })

        return responseLiveData
    }

    fun fillRequest(fillRequest: FillRequestData): LiveData<ResponseObject<RequestFilledResponse>>{
        val responseLiveData: MutableLiveData<ResponseObject<RequestFilledResponse>> = MutableLiveData()
        val backendRequest: Call<ResponseObject<RequestFilledResponse>> = dakaBackend.fillRequest(fillRequest)

        backendRequest.enqueue(object : Callback<ResponseObject<RequestFilledResponse>>{
            override fun onResponse(
                call: Call<ResponseObject<RequestFilledResponse>>,
                response: Response<ResponseObject<RequestFilledResponse>>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<ResponseObject<RequestFilledResponse>>,
                t: Throwable
            ) {
                t.printStackTrace()
            }
        })

        return responseLiveData
    }

    fun takeOffer(takeOffer: TakeOfferData): LiveData<ResponseObject<OfferTakenResponse>>{
        val responseLiveData: MutableLiveData<ResponseObject<OfferTakenResponse>> = MutableLiveData()
        val backendRequest: Call<ResponseObject<OfferTakenResponse>> = dakaBackend.takeOffer(takeOffer)

        backendRequest.enqueue(object : Callback<ResponseObject<OfferTakenResponse>>{
            override fun onResponse(
                call: Call<ResponseObject<OfferTakenResponse>>,
                response: Response<ResponseObject<OfferTakenResponse>>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<ResponseObject<OfferTakenResponse>>,
                t: Throwable
            ) {
                t.printStackTrace()
            }
        })

        return responseLiveData
    }

    companion object{
        private var INSTANCE: BackendFetcher? = null;
        fun initialize(context: Context){
            if(INSTANCE==null){
                INSTANCE = BackendFetcher(context)
            }
        }

        fun get(): BackendFetcher{
            return INSTANCE ?:
            throw IllegalStateException("BackendFetcher must be initialized first")
        }
    }

}