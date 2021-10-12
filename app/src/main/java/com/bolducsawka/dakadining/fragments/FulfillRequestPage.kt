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
import com.bolducsawka.dakadining.api.requestobjects.FillRequestData
import com.bolducsawka.dakadining.api.responseobjects.MealsUpdateReponse
import com.bolducsawka.dakadining.api.responseobjects.RequestFilledResponse
import com.bolducsawka.dakadining.api.responseobjects.ResponseObject
import com.bolducsawka.dakadining.dataobjects.Request
import com.bolducsawka.dakadining.dataobjects.SwipeObject
import com.bolducsawka.dakadining.dataobjects.User
import com.bolducsawka.dakadining.navigation.CommonCallbacks
import kotlinx.android.synthetic.main.fragment_create_profile_page.*
import java.text.SimpleDateFormat
import java.util.*

private const val ARG_USER = "user"
private const val ARG_OBJ = "object"
private const val TAG = "FulfillRequestPage"

class FulfillRequestPage: Fragment() {

    private var callbacks: CommonCallbacks? = null

    private lateinit var user: User
    private lateinit var request: Request

    private lateinit var txtNumSwipes: TextView
    private lateinit var txtDateTime: TextView
    private lateinit var txtReqPrice: TextView
    private lateinit var btnFulfillRequest: Button
    private lateinit var imgBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getSerializable(ARG_USER) as User
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
        txtDateTime.text = SimpleDateFormat("MM/dd/yy hh:mm", Locale.getDefault()).format(request.time)
        txtReqPrice.text = "$${request.price}"

        btnFulfillRequest.setOnClickListener {
            //Decrease the user's meals by the number of swipes requested
            val fillRequestLiveData: LiveData<ResponseObject<RequestFilledResponse>> = BackendFetcher.get().fillRequest(FillRequestData(request.requestID, user.userID))
            fillRequestLiveData.observe(viewLifecycleOwner, Observer {
                if(it.status == 200){
                    //success
                    Toast.makeText(context, "Request Filled", Toast.LENGTH_SHORT).show()
                    callbacks?.onBack()
                }else{
                    Toast.makeText(context, it.data.message, Toast.LENGTH_SHORT).show()
                }
            })

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
            FulfillRequestPage().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_USER, user)
                    putSerializable(ARG_OBJ, swipeObject)
                }
            }
    }

}