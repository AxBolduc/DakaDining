package com.bolducsawka.dakadining.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.bolducsawka.dakadining.R

class CreateNewOfferingPage : Fragment() {

    private lateinit var txtInputNumSwipes: EditText
    private lateinit var txtInputOfferingPrice: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_new_offering_page, container, false)

        txtInputNumSwipes = view.findViewById(R.id.txtInputNumSwipes)
        txtInputOfferingPrice = view.findViewById(R.id.txtInputOfferingPrice)

        return view
    }

    companion object{
        fun newInstance() =
            CreateNewOfferingPage().apply {

            }
    }

}