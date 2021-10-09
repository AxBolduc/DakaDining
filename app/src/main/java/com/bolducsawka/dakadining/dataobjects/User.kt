package com.bolducsawka.dakadining.dataobjects

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import org.bson.types.ObjectId
import java.io.Serializable
import java.lang.IllegalArgumentException

enum class MealPlan(val numMeals: Int){
    Nineteen(19),
    Fourteen(14),
    TwoHundred(200),
    None(0)
}

class User(var firstName: String = "", var lastName: String = "", var email: String = "", var pass: String = "", var role: String = ""): Serializable{

}