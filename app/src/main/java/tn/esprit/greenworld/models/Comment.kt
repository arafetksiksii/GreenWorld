package tn.esprit.greenworld.models
import java.io.Serializable
import java.util.Date


data class Comment(
    val _id: String,
    var Contenu: String,
    val date: Date,
    val eventID: String,
    val userID: String,
    val userName: String,
    val userImage: String?, // Ajout du champ userImage

    val __v: Int,

    ): Serializable
