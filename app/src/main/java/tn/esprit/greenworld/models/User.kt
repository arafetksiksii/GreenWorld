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
    val isValid:Boolean,
    val imageRes: Int,

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (idUser != other.idUser) return false
        if (nom != other.nom) return false
        if (prenom != other.prenom) return false
        if (dateNaissance != other.dateNaissance) return false
        if (adress != other.adress) return false
        if (cin != other.cin) return false
        if (userName != other.userName) return false
        if (email != other.email) return false
        if (paswword != other.paswword) return false
        if (isValid != other.isValid) return false
        if (imageRes != other.imageRes) return false

        return true
    }


    override fun toString(): String {
        return "User(idUser=$idUser, nom='$nom', prenom='$prenom', dateNaissance=$dateNaissance, adress='$adress', cin=$cin, userName='$userName', email='$email', paswword='$paswword', isValid=$isValid, imageRes=$imageRes)"
    }
}
