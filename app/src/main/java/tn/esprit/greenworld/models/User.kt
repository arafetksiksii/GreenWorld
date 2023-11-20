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
    val cin:Int,
    val userName:String,
    val email:String,
    val password:String,
    val lastPaswword:String,
    val isValid:Boolean,
    val imageRes: String,
    val resetCode: String,
    val role:Roles,
    val score :Int

) {



    override fun toString(): String {
        return "User(_id='$_id', nom='$nom', prenom='$prenom', dateNaissance=$dateNaissance, adress='$adress', cin=$cin, userName='$userName', email='$email', password='$password', lastPaswword='$lastPaswword', isValid=$isValid, imageRes='$imageRes', resetCode='$resetCode', role=$role, score='$score')"
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
    @SerializedName("_id") val id: String,
    val score: Int


)