package tn.esprit.greenworld.models

import org.osmdroid.util.GeoPoint
import java.io.Serializable

data class Event(
    val __v: Int,
    val _id: String,
    val titre: String,
    val datedebut:String,
    val datefin:String,
    val image: String,
    val lieu: String,
    val location: GeoPoint,
    val description: String,
    val nbparticipant: Int,
    val nbPlace: Int,
    val latitude: Double,
    val longitude: Double



) :Serializable