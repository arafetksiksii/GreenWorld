package tn.esprit.greenworld.models

import java.io.Serializable
import java.util.Date

data class Dechets (
    val __v: Int,
    val _id: String,
    val Type_dechets:String,
    val date_depot: String,
    val nombre_capacite:Int,
    val adresse:String,
    val file: List<String>?,
    val createdAt: String,
    val updatedAt: String

    ): Serializable
