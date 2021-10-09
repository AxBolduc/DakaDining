package com.bolducsawka.dakadining.viewmodels

import androidx.lifecycle.ViewModel
import com.bolducsawka.dakadining.dataobjects.Request
import java.util.*

class RequestListViewModel: ViewModel(){
    val requests = mutableListOf<Request>()

    init{
        for(i in 0 until 20){
            val request = Request("Bob Ross", i, i*10F, Date())
            requests += request
        }
    }
}