package com.bolducsawka.dakadining.api

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bolducsawka.dakadining.api.requestobjects.CreateUserRequest
import com.bolducsawka.dakadining.api.requestobjects.LoginCredentials
import com.bolducsawka.dakadining.api.responseobjects.LoginResponse
import com.bolducsawka.dakadining.api.responseobjects.MealsUpdateReponse
import com.bolducsawka.dakadining.api.responseobjects.ResponseObject
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