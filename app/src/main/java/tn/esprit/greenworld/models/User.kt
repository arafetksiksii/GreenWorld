package tn.esprit.greenworld.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val idUser: Long,
    val nom: String,
    val prenom : String,
    val dateNaissance :Date,
    val adress:String,
    val cin:Int,
    val userName:String,
    val email:String,
    val paswword:String,
    val lastPaswword:String,
    val isValid:Boolean,
    val imageRes: Int,
    val role:Roles

) {


    override fun toString(): String {
        return "User(idUser=$idUser, nom='$nom', prenom='$prenom', dateNaissance=$dateNaissance, adress='$adress', cin=$cin, userName='$userName', email='$email', paswword='$paswword', lastPaswword='$lastPaswword', isValid=$isValid, imageRes=$imageRes, role=$role)"
    }
}