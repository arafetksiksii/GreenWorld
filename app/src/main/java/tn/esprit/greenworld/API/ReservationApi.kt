package tn.esprit.greenworld.API

import tn.esprit.greenworld.models.EventList
import tn.esprit.greenworld.models.Reservation
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ReservationApi {
    @POST("api")
    fun addReservation(@Body reservation: Reservation): Call<Reservation>
    @GET("api")
    fun getAllReservations(): Call<List<Reservation>>
}