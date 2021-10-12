package com.bolducsawka.dakadining.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bolducsawka.dakadining.api.BackendFetcher
import com.bolducsawka.dakadining.api.responseobjects.GetOffersResponse
import com.bolducsawka.dakadining.api.responseobjects.ResponseObject

private const val TAG = "OfferListViewModel"

class OfferListViewModel: ViewModel(){
    var offers: LiveData<ResponseObject<GetOffersResponse>>? =  null

    fun getOffers(){
        offers = BackendFetcher.get().getOffers()
    }

}