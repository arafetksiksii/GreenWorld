package tn.esprit.greenworld.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.Date

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val _id: String,
    val nom: String,
    val prenom : String,
    val dateNaissance :Date,
    val adress:String,
    val cin:String,
    val userName:String,
    val email:String,
    val password:String,
    val lastPaswword:String,
    val isValid:Boolean,
    val imageRes: String,
    val resetCode: String,
    val role:Roles,
    val token:String,
    val score :Int,
    val loginCount:String,
    val totalTimeSpent:String

) {


    override fun toString(): String {
        return "User(_id='$_id', nom='$nom', prenom='$prenom', dateNaissance=$dateNaissance, address='$adress', cin='$cin', userName='$userName', email='$email', password='$password', lastPassword='$lastPaswword', isValid=$isValid, imageRes='$imageRes', resetCode='$resetCode', role=$role, score=$score, loginCount=$loginCount, totalTimeSpen=$totalTimeSpent)"
    }

}
data class User1(
    val email: String,
    val password: String
)

data class User2(
    val email: String,
    val password: String,
    val userName:String
)

data class User3(
    val email: String,

    )
data class User4(
    val id: String,
    val score: Int


)
data class UserR(val email: String, val rememberMe: Boolean)
data class UserData(

    val email: String,
    val nom: String,
    val prenom: String,
    val tokenfb: String
    // Include other user details as needed
)
data class User5(
    val id: String,
    val email: String,
    val nom: String,
    val prenom: String,
    val userName: String,
    val adress: String,
    val cin: String,
    val imageRes: String
) {
    constructor(email: String,id: String) : this(
        id = id,
        email = email,
        nom = "",
        prenom = "",
        userName = "",
        adress = "",
        cin = "",
        imageRes = ""
    )
}

