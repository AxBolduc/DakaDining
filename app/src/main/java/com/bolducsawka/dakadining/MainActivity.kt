package com.bolducsawka.dakadining

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.bolducsawka.dakadining.dataobjects.Offer
import com.bolducsawka.dakadining.fragments.*
import com.bolducsawka.dakadining.navigation.CommonCallbacks

class MainActivity : AppCompatActivity(), LoginPage.Callbacks, CreateProfilePage.Callbacks, OffersPage.Callbacks, CreateNewOfferingPage.Callbacks,  CommonCallbacks{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if(currentFragment == null){
            val fragment = LoginPage.newInstance()
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

    override fun onUserLoggedIn(seller: Boolean) {
        var fragment: Fragment? = null;
        if(seller){
             fragment = RequestsPage.newInstance()
        }else{
             fragment = OffersPage.newInstance()
        }
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onCancelCreateProfile() {
        supportFragmentManager.popBackStack()
    }

    override fun onProfileCreated(seller: Boolean) {
        var fragment: Fragment? = null;
        if(seller){
            fragment = RequestsPage.newInstance()
        }else{
            fragment = OffersPage.newInstance()
        }
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun swapPages(fromOffers: Boolean) {
        var fragment: Fragment? = null;
        if(fromOffers){
            fragment = RequestsPage.newInstance()
        }else{
            fragment = OffersPage.newInstance()
        }
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onAdd(fromOffers: Boolean) {
        var fragment: Fragment? = null;
        if(fromOffers){
            fragment = CreateNewOfferingPage.newInstance()
        }else{
            fragment = CreateNewRequestPage.newInstance()
        }
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onProfile(seller: Boolean) {
        var fragment: Fragment? = null;
        if(seller){
            fragment = SellerProfilePage.newInstance()
        }else{
            fragment = BuyerProfilePage.newInstance()
        }
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onBack() {
        supportFragmentManager.popBackStack()
    }

    override fun onLogout() {
        //User instance cleared out
        val fragment = LoginPage.newInstance()
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onNewSubmit(fromOffering: Boolean) {
        var fragment: Fragment? = null;
        if(fromOffering){
            fragment = OffersPage.newInstance()
        }else{
            fragment = RequestsPage.newInstance()
        }
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}