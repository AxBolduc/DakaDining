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
import com.bolducsawka.dakadining.dataobjects.Request
import com.bolducsawka.dakadining.dataobjects.SwipeObject
import com.bolducsawka.dakadining.navigation.CommonCallbacks
import kotlinx.android.synthetic.main.fragment_create_profile_page.*

private const val ARG_USERID = "userid"
private const val ARG_OBJ = "object"
private const val TAG = "FulfillRequestPage"

class FulfillRequestPage: Fragment() {

    private var callbacks: CommonCallbacks? = null

    private lateinit var userID: String
    private lateinit var request: Request

    private lateinit var txtNumSwipes: TextView
    private lateinit var txtDateTime: TextView
    private lateinit var txtReqPrice: TextView
    private lateinit var btnFulfillRequest: Button
    private lateinit var imgBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userID = it.getSerializable(ARG_USERID) as String
            request = it.getSerializable(ARG_OBJ) as Request
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
        val view =  inflater.inflate(R.layout.fragment_fulfill_request_page, container, false)

        txtNumSwipes = view.findViewById(R.id.txtNumSwipes)
        txtDateTime = view.findViewById(R.id.txtDateTime)
        txtReqPrice = view.findViewById(R.id.txtReqPrice)
        btnFulfillRequest = view.findViewById(R.id.btnFulfillRequest)
        imgBack = view.findViewById(R.id.imgBack)

        txtNumSwipes.text = "${request.meals} swipes"
        txtDateTime.text = "${request.time.month}/${request.time.date}/${request.time.year} ${request.time.hours}:${request.time.minutes}"
        txtReqPrice.text = "$${request.price}"

        btnFulfillRequest.setOnClickListener {
            //Decrease the user's meals by the number of swipes requested
            //remove request from database
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
            FulfillRequestPage().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_USERID, userID)
                    putSerializable(ARG_OBJ, swipeObject)
                }
            }
    }

}