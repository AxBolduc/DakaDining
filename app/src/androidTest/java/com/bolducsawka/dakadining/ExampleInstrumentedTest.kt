package com.bolducsawka.dakadining

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bolducsawka.dakadining.dataobjects.User
import com.bolducsawka.dakadining.fragments.LoginPage
import io.realm.Realm
import io.realm.kotlin.where
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import io.realm.mongodb.AppException
import io.realm.mongodb.sync.SyncConfiguration
import kotlinx.android.synthetic.main.fragment_login_page.*
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    private lateinit var loginPage: LoginPage;

    @Before
    fun createLoginPage(){
        Realm.init(InstrumentationRegistry.getInstrumentation().targetContext)
        dakaApp = App(
            AppConfiguration.Builder(BuildConfig.MONGODB_REALM_APP_ID)
                .defaultSyncErrorHandler{ session, error ->
                    Log.e("Tests", "Sync error: ${error.errorMessage}")
                }.build())

    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.bolducsawka.dakadining", appContext.packageName)
    }

    @Test
    fun createUser(){
        dakaApp.emailPassword.registerUserAsync("User", "Password"){
            if(it.isSuccess){
                assertTrue(true)
            }else{
                assertTrue(false)
            }
        }
    }

    @After
    fun after(){
        if(dakaApp.currentUser() != null){
            val config = SyncConfiguration.Builder(dakaApp.currentUser(), "user=${dakaApp.currentUser()!!.id}").build()

            val realm: Realm = Realm.getInstance(config)

            realm.executeTransactionAsync {
                val item = it.where<User>().equalTo("email", "User").findFirst()

                if (item != null) {
                    item.deleteFromRealm()
                }
            }

        }
    }
}