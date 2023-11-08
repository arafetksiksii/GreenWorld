package tn.esprit.green_world.interfaces

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import tn.esprit.green_world.models.Produit
import tn.esprit.green_world.models.ProduitList

interface ProduitApi {
    @GET("random-product")
    fun getRandomProduit(): Call<Produit>

    @GET("detail")
    fun getProduitDetails(@Query("id")id:String) :Call<Produit>

    @GET("a")
    fun getPopularProduit(): Call<List<Produit>>
    @GET("a")
    fun getProduit(): Call<ProduitList>

}