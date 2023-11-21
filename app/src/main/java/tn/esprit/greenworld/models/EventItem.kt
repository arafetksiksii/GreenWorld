package tn.esprit.greenworld.models

import org.osmdroid.util.GeoPoint

data class EventItem (

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
    )