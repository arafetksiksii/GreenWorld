package tn.esprit.green_world.interfaces

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import tn.esprit.green_world.models.Produit

interface ProduitApi {
    @GET("random-product")
    fun getRandomProduit(): Call<Produit>

    @GET("?")
    fun getProduitDetails(@Query("i")id:String) :Call<Produit>
}