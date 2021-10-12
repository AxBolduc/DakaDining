package com.bolducsawka.dakadining.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bolducsawka.dakadining.R
import com.bolducsawka.dakadining.dataobjects.Offer
import com.bolducsawka.dakadining.dataobjects.SwipeObject
import com.bolducsawka.dakadining.navigation.CommonCallbacks


private const val ARG_USERID = "userid"
private const val ARG_OBJ = "object"
private const val TAG = "PurchaseOfferingPage"

class PurchaseOfferingPage: Fragment() {

    private lateinit var userID: String
    private lateinit var offer: Offer

    private var callbacks: CommonCallbacks? = null

    private lateinit var txtNumSwipes: TextView
    private lateinit var txtOfferPrice: TextView
    private lateinit var btnAcceptOffer: Button
    private lateinit var imgBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            userID = it.getSerializable(ARG_USERID) as String
            offer = it.getSerializable(ARG_OBJ) as Offer
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as CommonCallbacks
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_purchase_offering, container, false)

        txtNumSwipes = view.findViewById(R.id.txtNumSwipes)
        txtOfferPrice = view.findViewById(R.id.txtOfferPrice)
        btnAcceptOffer = view.findViewById(R.id.btnAcceptOffer)
        imgBack = view.findViewById(R.id.imgBack)


        txtNumSwipes.text = "${offer.meals} swipes"
        txtOfferPrice.text = "$${offer.price}"

        btnAcceptOffer.setOnClickListener {
            //decrease meals in offerer's account
            //remove offer from database

            callbacks?.onBack()
        }

        imgBack.setOnClickListener {
            callbacks?.onBack()
        }


        return view
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    companion object{
        fun newInstance(userID: String, swipeObject: SwipeObject) =
            PurchaseOfferingPage().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_USERID, userID)
                    putSerializable(ARG_OBJ, swipeObject)
                }
            }
    }

}