package com.bolducsawka.dakadining.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bolducsawka.dakadining.R
import com.bolducsawka.dakadining.dataobjects.User

private const val ARG_USER = "user"

class CreateProfilePage : Fragment() {

    interface Callbacks{
        fun onCancelCreateProfile()
        fun onProfileCreated(user: User)
    }

    private var user: User? = null

    private var callbacks: Callbacks? = null

    private var meals: String = "None"

    private lateinit var txtInputFirstName: EditText;
    private lateinit var txtInputLastName: EditText;
    private lateinit var txtInputEmail: EditText;
    private lateinit var txtInputPassword: EditText;

    private lateinit var btnCreateSignUp: Button
    private lateinit var btnCreateCancel: Button

    private lateinit var spinnerMealPlan: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_create_profile_page, container, false)

        txtInputFirstName = view.findViewById(R.id.txtInputFirstName)
        txtInputLastName = view.findViewById(R.id.txtInputLastName)
        txtInputEmail = view.findViewById(R.id.txtInputEmail)
        txtInputPassword = view.findViewById(R.id.txtInputCreatePassword)

        btnCreateSignUp = view.findViewById(R.id.btnCreateSignUp)
        btnCreateCancel = view.findViewById(R.id.btnCreateCancel)

        spinnerMealPlan = view.findViewById(R.id.spinnerMealPlan)

        btnCreateSignUp.setOnClickListener {
            createUser()
        }

        spinnerMealPlan.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when(p0?.selectedItem.toString()){
                    "19/week" -> meals = "Nineteen"
                    "14/week" -> meals = "Fourteen"
                    "200/semester" -> meals = "TwoHundred"
                    "None" -> meals = "None"
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        btnCreateCancel.setOnClickListener {
            callbacks?.onCancelCreateProfile()
        }

        return view
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    fun createUser(){
        //TODO: Create user with API

        user = User(txtInputFirstName.text.toString(),
            txtInputLastName.text.toString(),
            txtInputEmail.text.toString(),
            txtInputPassword.text.toString(),
            "Seller")


        //Once create user is successful
        user?.let {
            callbacks?.onProfileCreated(it)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CreateProfilePage().apply {
            }
    }
}