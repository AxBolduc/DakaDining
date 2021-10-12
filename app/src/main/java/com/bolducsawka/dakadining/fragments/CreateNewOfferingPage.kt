package com.bolducsawka.dakadining.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.bolducsawka.dakadining.R
import com.bolducsawka.dakadining.api.BackendFetcher
import com.bolducsawka.dakadining.api.responseobjects.ResponseObject
import com.bolducsawka.dakadining.dataobjects.Offer
import com.bolducsawka.dakadining.navigation.CommonCallbacks
import kotlinx.android.synthetic.main.fragment_new_request_page.*

private const val ARG_USERID = "userid"
private const val TAG = "CreateNewOfferingPage"

class CreateNewOfferingPage : Fragment() {

    interface Callbacks{
        fun onNewSubmit(fromOffering:Boolean)
    }

    private lateinit var userID: String

    private lateinit var btnBack: ImageView
    private lateinit var btnSubmitOffering: Button

    private lateinit var txtInputNumSwipes: EditText
    private lateinit var txtInputOfferingPrice: EditText

    private var commonCallbacks: CommonCallbacks? = null
    private var callbacks: Callbacks? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userID = it.getSerializable(ARG_USERID) as String
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        commonCallbacks = context as CommonCallbacks
        callbacks = context as Callbacks
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_new_offering_page, container, false)

        btnBack = view.findViewById(R.id.imgBack)
        btnSubmitOffering = view.findViewById(R.id.btnFulfillRequest)

        txtInputNumSwipes = view.findViewById(R.id.txtInputNumSwipes)
        txtInputOfferingPrice = view.findViewById(R.id.txtInputOfferingPrice)

        btnBack.setOnClickListener {
            commonCallbacks?.onBack()
        }

        btnSubmitOffering.setOnClickListener {

            val newOfferLiveData: LiveData<ResponseObject<Offer>> = BackendFetcher.get().newOffer(
                Offer(
                    userID,
                    Integer.parseInt(txtInputNumSwipes.text.toString()),
                    Integer.parseInt(txtInputOfferingPrice.text.toString()),
                    false,
                    null
                )
            )
            newOfferLiveData.observe(viewLifecycleOwner, Observer {
                if(it.status == 200){
                    Toast.makeText(context, "Offer Created", Toast.LENGTH_SHORT).show()
                    callbacks?.onNewSubmit(true)
                }else{
                    Toast.makeText(context, "Failed to create offer", Toast.LENGTH_SHORT).show()
                }
            })

        }


        return view
    }

    override fun onDetach() {
        super.onDetach()
        commonCallbacks = null
        callbacks = null
    }

    companion object{
        fun newInstance(userID: String) =
            CreateNewOfferingPage().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_USERID, userID)
                }

            }
    }

}