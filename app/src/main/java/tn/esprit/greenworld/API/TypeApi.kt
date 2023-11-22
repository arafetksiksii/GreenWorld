package tn.esprit.greenworld.API

import tn.esprit.greenworld.models.TypeList
import retrofit2.Call
import retrofit2.http.GET

interface TypeApi {

    @GET("type")
    fun getType(): Call<TypeList>

}