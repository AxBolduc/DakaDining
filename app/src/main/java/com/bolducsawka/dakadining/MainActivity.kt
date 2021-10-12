package com.bolducsawka.dakadining

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.bolducsawka.dakadining.api.BackendFetcher
import com.bolducsawka.dakadining.dataobjects.Offer
import com.bolducsawka.dakadining.dataobjects.SwipeObject
import com.bolducsawka.dakadining.dataobjects.User
import com.bolducsawka.dakadining.fragments.*
import com.bolducsawka.dakadining.navigation.CommonCallbacks

class MainActivity : AppCompatActivity(), LoginPage.Callbacks, CreateProfilePage.Callbacks, OffersPage.Callbacks, CreateNewOfferingPage.Callbacks,  CommonCallbacks{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        BackendFetcher.initialize(this)

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

    override fun onAdd(fromOffers: Boolean, userID: String) {
        var fragment: Fragment? = null;
        if(fromOffers){
            fragment = CreateNewOfferingPage.newInstance(userID)
        }else{
            fragment = CreateNewRequestPage.newInstance(userID)
        }
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onProfile(user: User) {
        var fragment: Fragment? = null;
        if(user.role.equals("Seller")){
            fragment = SellerProfilePage.newInstance(user)
        }else{
            fragment = BuyerProfilePage.newInstance(user)
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
        for (i in 0..supportFragmentManager.backStackEntryCount){
            supportFragmentManager.popBackStack()
        }

        val fragment = LoginPage.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onObjectClick(user: User, fromOffers: Boolean, swipeObject: SwipeObject) {
        var fragment: Fragment?= null
        if(fromOffers){
            fragment = PurchaseOfferingPage.newInstance(user, swipeObject)
        }else{
            fragment = FulfillRequestPage.newInstance(user, swipeObject)
        }

        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onNewSubmit(fromOffering: Boolean) {
        supportFragmentManager.popBackStack()
    }
}