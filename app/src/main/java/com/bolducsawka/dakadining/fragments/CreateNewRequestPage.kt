package com.bolducsawka.dakadining.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.InputType
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
import com.bolducsawka.dakadining.dataobjects.Request
import com.bolducsawka.dakadining.navigation.CommonCallbacks
import java.util.*

private const val ARG_USERID = "userid"
private const val TAG = "CreateNewRequestPage"

class CreateNewRequestPage: Fragment() {


    private lateinit var userID: String
    private var date: Date = Date()

    private lateinit var btnBack: ImageView
    private lateinit var btnSubmitRequest: Button

    private lateinit var txtInputNumSwipes: EditText
    private lateinit var txtInputDate: EditText
    private lateinit var txtInputTime: EditText
    private lateinit var txtInputPrice: EditText

    private lateinit var datePicker: DatePickerDialog
    private lateinit var timePicker: TimePickerDialog

    private var commonCallbacks: CommonCallbacks? = null
    private var callbacks: CreateNewOfferingPage.Callbacks? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userID = it.getSerializable(ARG_USERID) as String
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        commonCallbacks = context as CommonCallbacks
        callbacks = context as CreateNewOfferingPage.Callbacks
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_new_request_page, container, false)

        btnBack = view.findViewById(R.id.imgBack)
        btnSubmitRequest = view.findViewById(R.id.btnSubmitRequest)

        txtInputNumSwipes = view.findViewById(R.id.txtInputNumSwipes)
        txtInputDate = view.findViewById(R.id.txtInputDate)
        txtInputTime = view.findViewById(R.id.txtInputTime)
        txtInputPrice = view.findViewById(R.id.txtInputPrice)

        txtInputDate.inputType = InputType.TYPE_NULL
        txtInputTime.inputType = InputType.TYPE_NULL

        txtInputDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)


            datePicker = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                date.let {
                    it.year = year;
                    it.month = month+1;
                    it.date = day
                }
                txtInputDate.setText("${month+1}/${day}/${year}")
            }, year, month, day)

            datePicker.show()
        }

        txtInputTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val min = calendar.get(Calendar.MINUTE)

            timePicker = TimePickerDialog(requireContext(), TimePickerDialog.OnTimeSetListener { timePicker, hour, min ->
                date.let {
                    it.hours = hour
                    it.minutes = min
                }
                txtInputTime.setText("${hour}:${min}")
            }, hour, min, false)

            timePicker.show()
        }

        btnBack.setOnClickListener {
            commonCallbacks?.onBack()
        }

        btnSubmitRequest.setOnClickListener {
            val newRequestResponse: LiveData<ResponseObject<Request>> = BackendFetcher.get().newRequest(
                Request(
                    userID,
                    Integer.parseInt(txtInputNumSwipes.text.toString()),
                    Integer.parseInt(txtInputPrice.text.toString()),
                    date,
                    false,
                    null,
                    null
                )
            )

            newRequestResponse.observe(viewLifecycleOwner, Observer {
                if(it.status == 200) {
                    Toast.makeText(context, "Request Created", Toast.LENGTH_SHORT).show()
                    callbacks?.onNewSubmit(false)
                }else{
                    Toast.makeText(context, "Failed to create request", Toast.LENGTH_SHORT).show()
                }
            })
        }
        return view
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
        commonCallbacks = null
    }

    companion object {
        @JvmStatic
        fun newInstance(userID: String) =
            CreateNewRequestPage().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_USERID, userID)
                }
            }
    }

}