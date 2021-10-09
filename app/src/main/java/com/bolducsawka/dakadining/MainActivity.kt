package com.bolducsawka.dakadining

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bolducsawka.dakadining.fragments.*

class MainActivity : AppCompatActivity(), LoginPage.Callbacks, CreateProfilePage.Callbacks{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if(currentFragment == null){
//            val fragment = LoginPage.newInstance()
            val fragment = SellerProfilePage.newInstance()
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit()
        }
    }

    override fun onCreateUser() {
        val fragment = CreateProfilePage.newInstance()
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onCancelCreateProfile() {
        supportFragmentManager.popBackStack()
    }
}