package tn.esprit.greenworld.models

data class Reservation(
    val date_reservation: String = "2023-11-02T00:00:00Z",

    val eventID: String,
    val userID: String

)
