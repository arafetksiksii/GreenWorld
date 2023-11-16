package com.example.testtt.utils

import com.example.testtt.API.EventApi
import com.example.testtt.API.TypeApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api:EventApi by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.1.105:9090/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EventApi::class.java)

    }
    val apii:TypeApi by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.1.105:9090/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TypeApi::class.java)

    }
}