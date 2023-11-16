package com.example.testtt.viewModel.API

import com.example.testtt.models.EventList
import retrofit2.Call
import retrofit2.http.GET

interface EventApi {

    @GET("event")
    fun getEvent(): Call<EventList>

}