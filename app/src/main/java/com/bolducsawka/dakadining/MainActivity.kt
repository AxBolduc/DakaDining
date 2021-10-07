package com.bolducsawka.dakadining

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bolducsawka.dakadining.dataobjects.User
import com.bolducsawka.dakadining.fragments.CreateProfilePage
import com.bolducsawka.dakadining.fragments.LoginPage
import io.realm.Realm
import io.realm.RealmConfiguration

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if(currentFragment == null){
            val fragment = LoginPage.newInstance()
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit()
        }
    }
}