package com.example.testtt.API

import com.example.testtt.models.TypeList
import retrofit2.Call
import retrofit2.http.GET

interface TypeApi {

    @GET("type")
    fun getType(): Call<TypeList>

}