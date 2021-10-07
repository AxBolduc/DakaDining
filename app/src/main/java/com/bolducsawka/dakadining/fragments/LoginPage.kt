package com.bolducsawka.dakadining.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.bolducsawka.dakadining.R
import com.bolducsawka.dakadining.dakaApp
import io.realm.mongodb.Credentials

class LoginPage : Fragment() {

    private lateinit var txtInputUsername: EditText
    private lateinit var txtInputPassword: EditText

    private lateinit var btnLogin: Button
    private lateinit var btnSignUp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            signUp(txtInputUsername.text.toString(), txtInputPassword.text.toString())
        }

        return view
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

    private fun login(user: String, pass: String){
        //TODO: Login stuff

        btnLogin.isEnabled = false
        btnSignUp.isEnabled = false

        val creds = Credentials.emailPassword(user, pass)

        dakaApp.loginAsync(creds){
            btnLogin.isEnabled = true
            btnSignUp.isEnabled = true

            if(!it.isSuccess){
                Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, "Login Success", Toast.LENGTH_SHORT).show()
            }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            LoginPage().apply {

            }
    }
}