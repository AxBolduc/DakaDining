package com.bolducsawka.dakadining.dataobjects

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class User: RealmObject(){
    @PrimaryKey
    lateinit var _id: String

    @Required
    lateinit var email: String
}