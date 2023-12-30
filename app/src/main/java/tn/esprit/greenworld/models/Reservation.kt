package tn.esprit.greenworld.models
import com.fasterxml.jackson.annotation.JsonFormat

import java.util.Date

data class Reservation(
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")

    val date_reservation: Date,

    val eventID: String,
    val userID: String

)