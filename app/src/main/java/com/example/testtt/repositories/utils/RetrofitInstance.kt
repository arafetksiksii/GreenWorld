package com.example.testtt.repositories.utils

import com.example.testtt.viewModel.API.EventApi
import com.example.testtt.viewModel.API.TypeApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: EventApi by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:9090/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EventApi::class.java)
    }
    val apii: TypeApi by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:9090/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TypeApi::class.java)

    }
}