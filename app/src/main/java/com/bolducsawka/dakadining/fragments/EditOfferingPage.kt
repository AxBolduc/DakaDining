package com.bolducsawka.dakadining.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bolducsawka.dakadining.R
import com.bolducsawka.dakadining.dataobjects.Offer
import com.bolducsawka.dakadining.dataobjects.SwipeObject
import com.bolducsawka.dakadining.dataobjects.User
import com.bolducsawka.dakadining.navigation.CommonCallbacks

private const val ARG_USER = "user"
private const val ARG_OBJ = "object"
private const val TAG = "CreateNewOfferingPage"

class EditOfferingPage : Fragment() {


    private lateinit var user: User
    private lateinit var offer: Offer

    private lateinit var btnBack: ImageView
    private lateinit var btnSubmitOffering: Button
    private lateinit var btnDeleteOffer: Button

    private lateinit var txtInputNumSwipes: EditText
    private lateinit var txtInputOfferingPrice: EditText

    private var commonCallbacks: CommonCallbacks? = null
    private var callbacks: CreateNewOfferingPage.Callbacks? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getSerializable(ARG_USER) as User
            offer  = it.getSerializable(ARG_OBJ) as Offer
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        commonCallbacks = context as CommonCallbacks
        callbacks = context as CreateNewOfferingPage.Callbacks
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_edit_offering_page, container, false)

        btnBack = view.findViewById(R.id.imgBack)
        btnSubmitOffering = view.findViewById(R.id.btnSubmitOffer)
        btnDeleteOffer = view.findViewById(R.id.btnDeleteOffer)

        txtInputNumSwipes = view.findViewById(R.id.txtInputNumSwipes)
        txtInputOfferingPrice = view.findViewById(R.id.txtInputOfferingPrice)

        txtInputNumSwipes.setText(offer.meals.toString())
        txtInputOfferingPrice.setText(offer.price.toString())


        btnBack.setOnClickListener {
            commonCallbacks?.onBack()
        }

        btnSubmitOffering.setOnClickListener {
            //TODO: Update Offer
        }

        btnDeleteOffer.setOnClickListener {
            //TODO: Delete offer
        }


        return view
    }

    override fun onDetach() {
        super.onDetach()
        commonCallbacks = null
        callbacks = null
    }

    companion object{
        fun newInstance(user: User, swipeObject: SwipeObject) =
            EditOfferingPage().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_USER, user)
                    putSerializable(ARG_OBJ, swipeObject)
                }

            }
    }

}