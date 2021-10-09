package com.bolducsawka.dakadining.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bolducsawka.dakadining.R

class PurchaseOfferingPage: Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_requests_page, container, false)

        return view
    }

    companion object{
        fun newInstance() =
            PurchaseOfferingPage.apply {

            }
    }

}