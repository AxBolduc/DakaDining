package com.bolducsawka.dakadining

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.bolducsawka.dakadining.api.BackendFetcher
import com.bolducsawka.dakadining.dataobjects.Offer
import com.bolducsawka.dakadining.dataobjects.User
import com.bolducsawka.dakadining.fragments.*
import com.bolducsawka.dakadining.navigation.CommonCallbacks

class MainActivity : AppCompatActivity(), LoginPage.Callbacks, CreateProfilePage.Callbacks, OffersPage.Callbacks, CreateNewOfferingPage.Callbacks,  CommonCallbacks{

    private lateinit var tempUser: User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        BackendFetcher.initialize(this)

        tempUser = User("12", "alex", "bolduc", "aebolduc@wpi.edu", "PASS", 10, 19, "Seller", null)

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

    override fun onUserLoggedIn(user: User) {
        var fragment: Fragment? = null;
        if(user.role.equals("Seller")){
             fragment = RequestsPage.newInstance(user)
        }else{
             fragment = OffersPage.newInstance(user)
        }
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onCancelCreateProfile() {
        supportFragmentManager.popBackStack()
    }

    override fun onProfileCreated(user: User) {
        var fragment: Fragment? = null;
        if(user.role.equals("Seller")){
            fragment = RequestsPage.newInstance(user)
        }else{
            fragment = OffersPage.newInstance(user)
        }
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun swapPages(user: User, fromOffers: Boolean) {
        var fragment: Fragment? = null;
        if(fromOffers){
            fragment = RequestsPage.newInstance(user)
        }else{
            fragment = OffersPage.newInstance(user)
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

    override fun onProfile(user: User) {
        var fragment: Fragment? = null;
        if(user.role.equals("Seller")){
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
        supportFragmentManager.popBackStack()
    }
}