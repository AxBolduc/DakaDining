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
import com.bolducsawka.dakadining.dakaApp
import com.bolducsawka.dakadining.dataobjects.User
import io.realm.mongodb.Credentials

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
    
    private fun signUp(user: String, pass: String){
        btnLogin.isEnabled = false
        btnSignUp.isEnabled = false
        
        dakaApp.emailPassword.registerUserAsync(user, pass){
            btnLogin.isEnabled = true
            btnSignUp.isEnabled = true
            
            if(!it.isSuccess){
                Toast.makeText(context, "Registration failed", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, "Registration Success", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun login(email: String, pass: String){

        btnLogin.isEnabled = false
        btnSignUp.isEnabled = false

        val loginResponseLiveData: LiveData<ResponseObject<LoginResponse>> = BackendFetcher.get().loginUser(LoginCredentials(email, pass))
        loginResponseLiveData.observe(viewLifecycleOwner, Observer {
            if(it.status == 200) {
                val userResponseLiveData: LiveData<ResponseObject<User>> =
                    BackendFetcher.get().getUserBySessionID(it.data.sessionID)
                userResponseLiveData.observe(viewLifecycleOwner, Observer {
                    if(it.status == 200) {
                        Log.d(TAG, it.data.toString())
                        user = it.data
                        callbacks?.onUserLoggedIn(it.data)
                    }else{
                        it.data.message?.let { it1 -> Log.d(TAG, it1) }
                    }
                })
            }
            else{
                Log.d(TAG, it.data.message)
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