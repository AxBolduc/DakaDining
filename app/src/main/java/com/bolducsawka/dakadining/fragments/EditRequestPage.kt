package com.bolducsawka.dakadining.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.util.Log
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
import com.bolducsawka.dakadining.api.responseobjects.ResponseData
import com.bolducsawka.dakadining.api.responseobjects.ResponseObject
import com.bolducsawka.dakadining.dataobjects.Request
import com.bolducsawka.dakadining.dataobjects.SwipeObject
import com.bolducsawka.dakadining.dataobjects.User
import com.bolducsawka.dakadining.navigation.CommonCallbacks
import java.text.SimpleDateFormat
import java.util.*

private const val ARG_USER = "user"
private const val ARG_OBJ = "object"
private const val TAG = "CreateNewRequestPage"

class EditRequestPage: Fragment() {


    private lateinit var user: User
    private lateinit var request: Request
    private var date: Date = Date()

    private lateinit var btnBack: ImageView
    private lateinit var btnSubmitRequest: Button
    private lateinit var btnDeleteRequest: Button

    private lateinit var txtInputNumSwipes: EditText
    private lateinit var txtInputDate: EditText
    private lateinit var txtInputTime: EditText
    private lateinit var txtInputPrice: EditText

    private lateinit var datePicker: DatePickerDialog
    private lateinit var timePicker: TimePickerDialog

    private var commonCallbacks: CommonCallbacks? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getSerializable(ARG_USER) as User
            request = it.getSerializable(ARG_OBJ) as Request
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        commonCallbacks = context as CommonCallbacks
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_edit_request_page, container, false)

        btnBack = view.findViewById(R.id.imgBack)
        btnSubmitRequest = view.findViewById(R.id.btnSubmitRequest)
        btnDeleteRequest = view.findViewById(R.id.btnDeleteRequest)

        txtInputNumSwipes = view.findViewById(R.id.txtInputNumSwipes)
        txtInputDate = view.findViewById(R.id.txtInputDate)
        txtInputTime = view.findViewById(R.id.txtInputTime)
        txtInputPrice = view.findViewById(R.id.txtInputPrice)

        txtInputNumSwipes.setText(request.meals.toString())
        txtInputDate.setText("${request.time.month+1}/${request.time.day}/${request.time.year}")
        txtInputTime.setText("${request.time.hours}:${request.time.minutes}")
        txtInputPrice.setText(request.price.toString())

        txtInputDate.inputType = InputType.TYPE_NULL
        txtInputTime.inputType = InputType.TYPE_NULL

        txtInputDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)


            datePicker = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                date.let {
                    it.year = year
                    it.month = month
                    it.date = day
                }
                txtInputDate.setText(SimpleDateFormat("MM/dd/yy", Locale.getDefault()).format(date))
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
                txtInputTime.setText(SimpleDateFormat("hh:mm").format(date))
            }, hour, min, false)

            timePicker.show()
        }

        btnBack.setOnClickListener {
            commonCallbacks?.onBack()
        }

        btnSubmitRequest.setOnClickListener {
            Log.d("LMAO", request.requestID)
            val updateRequestLiveData: LiveData<ResponseObject<Request>> = BackendFetcher.get().updateRequest(
                Request(
                    request.requestID,
                    request.requester,
                    Integer.parseInt(txtInputNumSwipes.text.toString()),
                    Integer.parseInt(txtInputPrice.text.toString()),
                    date,
                    false,
                    null,
                    null
                )
            )

            updateRequestLiveData.observe(viewLifecycleOwner, Observer {
                if(it.status == 200){
                    Toast.makeText(context, "Request Updated Successfully", Toast.LENGTH_SHORT).show()
                    commonCallbacks?.onBack()
                }else{
                    Toast.makeText(context, it.data.message, Toast.LENGTH_SHORT).show()
                }
            })
        }

        btnDeleteRequest.setOnClickListener {
            Log.d("LMAO", request.requestID)
            val updateRequestLiveData: LiveData<ResponseObject<Request>> = BackendFetcher.get().deleteRequest(request)

            updateRequestLiveData.observe(viewLifecycleOwner, Observer {
                if(it.status == 200){
                    Toast.makeText(context, "Request Deleted", Toast.LENGTH_SHORT).show()
                    commonCallbacks?.onBack()
                }else{
                    Toast.makeText(context, it.data.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
        return view
    }

    override fun onDetach() {
        super.onDetach()
        commonCallbacks = null
    }

    companion object {
        @JvmStatic
        fun newInstance(user: User, swipeObject: SwipeObject) =
            EditRequestPage().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_USER, user)
                    putSerializable(ARG_OBJ, swipeObject)
                }
            }
    }

}