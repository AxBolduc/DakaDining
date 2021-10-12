package com.bolducsawka.dakadining.fragments

import android.content.Context
import android.os.Bundle
import android.telecom.Call
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.bolducsawka.dakadining.R
import com.bolducsawka.dakadining.api.BackendFetcher
import com.bolducsawka.dakadining.api.requestobjects.LoginCredentials
import com.bolducsawka.dakadining.api.responseobjects.LoginResponse
import com.bolducsawka.dakadining.api.responseobjects.ResponseObject
import com.bolducsawka.dakadining.dataobjects.User

private const val TAG = "LoginPage"

class LoginPage : Fragment() {

    interface Callbacks {
        fun onCreateUser()
        fun onUserLoggedIn(user: User)
    }

    private var user: User? = null

    private var callbacks: Callbacks? = null

    private lateinit var txtInputUsername: EditText
    private lateinit var txtInputPassword: EditText

    private lateinit var btnLogin: Button
    private lateinit var btnSignUp: Button

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login_page, container, false)

        txtInputUsername = view.findViewById(R.id.txtInputUsername)
        txtInputPassword = view.findViewById(R.id.txtInputPassword)

        btnLogin = view.findViewById(R.id.btnLogin)
        btnSignUp = view.findViewById(R.id.btnSignUp)

        btnLogin.setOnClickListener {
            login(txtInputUsername.text.toString(), txtInputPassword.text.toString())
        }

        btnSignUp.setOnClickListener {
            callbacks?.onCreateUser()
        }

        return view
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun login(email: String, pass: String){

        btnLogin.isEnabled = false
        btnSignUp.isEnabled = false


        //Send login request
        val loginResponseLiveData: LiveData<ResponseObject<LoginResponse>> = BackendFetcher.get().loginUser(LoginCredentials(email, pass))
        loginResponseLiveData.observe(viewLifecycleOwner, Observer { loginResponse ->
            if(loginResponse.status == 200) {
                //success

                //Get authenticated user from session id to pass through application
                val userResponseLiveData: LiveData<ResponseObject<User>> =
                    BackendFetcher.get().getUserBySessionID(loginResponse.data.sessionID)
                userResponseLiveData.observe(viewLifecycleOwner, Observer { userResponse ->
                    if(userResponse.status == 200) {
                        //Success
                        user = userResponse.data
                        callbacks?.onUserLoggedIn(userResponse.data)
                    }else{
                        //fail
                        Toast.makeText(context, userResponse.data.message, Toast.LENGTH_SHORT).show()
                    }
                })
            }
            else{

                //Failure
                Toast.makeText(context, loginResponse.data.message, Toast.LENGTH_SHORT).show()
                btnSignUp.isEnabled = true
                btnLogin.isEnabled = true
            }
        })

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            LoginPage().apply {

            }
    }
}