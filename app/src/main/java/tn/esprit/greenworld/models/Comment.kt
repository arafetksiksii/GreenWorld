package tn.esprit.greenworld.models
import java.io.Serializable
import java.util.Date


data class Comment(
    val _id: String,
    val Contenu: String,
    val date: Date,
    val eventID: String,
    val userID: String,
    val __v: Int,

): Serializable
