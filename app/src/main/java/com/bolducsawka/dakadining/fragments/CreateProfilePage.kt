package com.bolducsawka.dakadining.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.bolducsawka.dakadining.R


class CreateProfilePage : Fragment() {

    interface Callbacks{
        fun onCancelCreateProfile()
    }

    private var callbacks: Callbacks? = null

    private lateinit var txtInputFirstName: EditText;
    private lateinit var txtInputLastName: EditText;
    private lateinit var txtInputEmail: EditText;
    private lateinit var txtInputPassword: EditText;

    private lateinit var btnCreateSignUp: Button
    private lateinit var btnCreateCancel: Button

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

        btnCreateCancel.setOnClickListener {
            callbacks?.onCancelCreateProfile()
        }

        return view
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CreateProfilePage().apply {

            }
    }
}