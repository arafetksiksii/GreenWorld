package tn.esprit.greenworld.API

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import tn.esprit.greenworld.models.pointCollecte

interface pointApi {
  //  @POST("/point")
  //  fun createPoint(@Body point: pointCollecte): Call<pointCollecte>

    @GET("/point")
    fun getAllPoints(): Call<List<pointCollecte>>
}