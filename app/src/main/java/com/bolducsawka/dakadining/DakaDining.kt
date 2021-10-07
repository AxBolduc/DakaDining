package com.bolducsawka.dakadining

import android.app.Application
import android.util.Log
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.log.LogLevel
import io.realm.log.RealmLog
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration

lateinit var dakaApp: App

private const val TAG = "DakaDining"

class DakaDining: Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)

        dakaApp = App(
            AppConfiguration.Builder(BuildConfig.MONGODB_REALM_APP_ID)
                .defaultSyncErrorHandler{ session, error ->
                    Log.e(TAG, "Sync error: ${error.errorMessage}")
                }.build())

        if(BuildConfig.DEBUG){
            RealmLog.setLevel(LogLevel.ALL)
        }

        Log.v(TAG, "Initialized the Realm App configuration for: ${dakaApp.configuration.appId}")

    }


}