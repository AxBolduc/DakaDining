package com.bolducsawka.dakadining.navigation

import com.bolducsawka.dakadining.dataobjects.SwipeObject
import com.bolducsawka.dakadining.dataobjects.User

/**
 * Callback functions that are useful in many fragments
 */
interface CommonCallbacks {
    fun onBack()
    fun onLogout()
    fun onObjectClick(user: User, fromOffers: Boolean, swipeObject: SwipeObject, toEdit: Boolean)
}