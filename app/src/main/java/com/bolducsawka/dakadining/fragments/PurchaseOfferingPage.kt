package com.bolducsawka.dakadining.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.bolducsawka.dakadining.R
import com.bolducsawka.dakadining.api.BackendFetcher
import com.bolducsawka.dakadining.api.requestobjects.TakeOfferData
import com.bolducsawka.dakadining.api.responseobjects.MealsUpdateReponse
import com.bolducsawka.dakadining.api.responseobjects.OfferTakenResponse
import com.bolducsawka.dakadining.api.responseobjects.ResponseObject
import com.bolducsawka.dakadining.dataobjects.Offer
import com.bolducsawka.dakadining.dataobjects.SwipeObject
import com.bolducsawka.dakadining.dataobjects.User
import com.bolducsawka.dakadining.navigation.CommonCallbacks


private const val ARG_USER = "userid"
private const val ARG_OBJ = "object"
private const val TAG = "PurchaseOfferingPage"

class PurchaseOfferingPage: Fragment() {

    private lateinit var user: User
    private lateinit var offer: Offer

    private var callbacks: CommonCallbacks? = null

    private lateinit var txtNumSwipes: TextView
    private lateinit var txtOfferPrice: TextView
    private lateinit var btnAcceptOffer: Button
    private lateinit var imgBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            user = it.getSerializable(ARG_USER) as User
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
            if(user.role == "Buyer") {

                val takeOfferLiveData: LiveData<ResponseObject<OfferTakenResponse>> =
                    BackendFetcher.get().takeOffer(
                        TakeOfferData(offer.offerID, user.userID)
                    )
                takeOfferLiveData.observe(viewLifecycleOwner, Observer {
                    if (it.status == 200) {
                        Toast.makeText(context, "RequestFilled", Toast.LENGTH_SHORT).show()
                        callbacks?.onBack()
                    }
                })
            }
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
        fun newInstance(user: User, swipeObject: SwipeObject) =
            PurchaseOfferingPage().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_USER, user)
                    putSerializable(ARG_OBJ, swipeObject)
                }
            }
    }

}