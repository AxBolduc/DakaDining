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

class MainActivity : AppCompatActivity(), LoginPage.Callbacks, CreateProfilePage.Callbacks, OffersPage.Callbacks,   CommonCallbacks{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Initialize BackendFetcher singleton
        BackendFetcher.initialize(this)


        //Initialize fragment navigation
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if(currentFragment == null){
            val fragment = LoginPage.newInstance()
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit()
        }
    }

    /**
     * Fragment navigation callbacks
     */

    override fun onCreateUser() {
        val fragment = CreateProfilePage.newInstance()
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onUserLoggedIn(user: User) {
        var fragment: Fragment? = null

        //If the user is a seller send them to the requests page, or else to the offers page
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
        var fragment: Fragment? = null

        //If the user is a seller send them to the requests page, or else to the offers page
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
        var fragment: Fragment? = null

        //Send user to the page opposite they one they were on
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
        var fragment: Fragment? = null

        //send to add page based on the page they started
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
        var fragment: Fragment? = null

        //Send user to the correct profile fragment depending on the type of user they are
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
        //Go back a page in the backstack
        supportFragmentManager.popBackStack()
    }

    override fun onLogout() {

        //Logout
        for (i in 0..supportFragmentManager.backStackEntryCount){
            supportFragmentManager.popBackStack()
        }

        val fragment = LoginPage.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onObjectClick(user: User, fromOffers: Boolean, swipeObject: SwipeObject, toEdit: Boolean) {
        var fragment: Fragment?= null

        //Send user to the edit page based on type of object they are editing
        //Or send them to the purchase object page based on the object they click on and their role
        if(!toEdit) {
            if (fromOffers) {
                fragment = PurchaseOfferingPage.newInstance(user, swipeObject)
            } else {
                fragment = FulfillRequestPage.newInstance(user, swipeObject)
            }
        }else{
            if(fromOffers){
                fragment = EditOfferingPage.newInstance(user, swipeObject)
            }else{
                fragment = EditRequestPage.newInstance(user, swipeObject)
            }
        }

        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

}