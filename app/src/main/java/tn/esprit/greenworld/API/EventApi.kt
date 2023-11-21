package tn.esprit.greenworld.API

import tn.esprit.greenworld.models.Comment
import tn.esprit.greenworld.models.Event
import tn.esprit.greenworld.models.EventList
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface EventApi {

    @GET("event")
    fun getEvent(): Call<EventList>
    @GET("/event/detail")
    fun getEventDetails(@Query("id")id:String) :Call<Event>
}
