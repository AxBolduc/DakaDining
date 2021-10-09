package com.bolducsawka.dakadining.viewmodels

import androidx.lifecycle.ViewModel
import com.bolducsawka.dakadining.dataobjects.Offer
import com.bolducsawka.dakadining.dataobjects.Request
import java.util.*

class OfferListViewModel: ViewModel(){
    val offers = mutableListOf<Offer>()

    init{
        for(i in 0 until 20){
            val offer = Offer("Bob Ross", i, i*10F)
            offers += offer
        }
    }
}