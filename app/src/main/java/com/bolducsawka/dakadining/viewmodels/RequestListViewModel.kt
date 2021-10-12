package com.bolducsawka.dakadining.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bolducsawka.dakadining.api.BackendFetcher
import com.bolducsawka.dakadining.api.responseobjects.GetRequestsResponse
import com.bolducsawka.dakadining.api.responseobjects.ResponseObject
import com.bolducsawka.dakadining.dataobjects.Request
import retrofit2.Response
import java.util.*

class RequestListViewModel: ViewModel(){
    var requests: LiveData<ResponseObject<GetRequestsResponse>>? = null

    fun getRequests(){
        requests = BackendFetcher.get().getRequests()
    }
}