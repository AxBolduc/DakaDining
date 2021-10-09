package com.bolducsawka.dakadining.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import com.bolducsawka.dakadining.R
import java.util.*

class CreateNewRequestFragment: Fragment() {

    private lateinit var txtInputNumSwipes: EditText
    private lateinit var txtInputDate: EditText
    private lateinit var txtInputTime: EditText
    private lateinit var txtInputPrice: EditText

    private lateinit var datePicker: DatePickerDialog
    private lateinit var timePicker: TimePickerDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_new_request_page, container, false)

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
                txtInputDate.setText("${month}/${day}/${year}")
            }, year, month, day)

            datePicker.show()
        }

        txtInputTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val min = calendar.get(Calendar.MINUTE)

            timePicker = TimePickerDialog(requireContext(), TimePickerDialog.OnTimeSetListener { timePicker, hour, min ->
                txtInputTime.setText("${hour}:${min}")
            }, hour, min, false)

            timePicker.show()
        }
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CreateNewRequestFragment().apply {

            }
    }

}