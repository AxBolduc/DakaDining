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
import com.bolducsawka.dakadining.navigation.CommonCallbacks

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

        btnBack = view.findViewById(R.id.btnBack)
        btnSubmitOffering = view.findViewById(R.id.btnSubmitOffering)

        txtInputNumSwipes = view.findViewById(R.id.txtInputNumSwipes)
        txtInputOfferingPrice = view.findViewById(R.id.txtInputOfferingPrice)

        btnBack.setOnClickListener {
            commonCallbacks?.onBack()
        }

        btnSubmitOffering.setOnClickListener {


            callbacks?.onNewSubmit(true)
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