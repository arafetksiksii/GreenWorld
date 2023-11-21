package tn.esprit.greenworld.interfaces

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import tn.esprit.greenworld.models.Produit
import tn.esprit.greenworld.models.ProduitList

interface ProduitApi {
    @GET("produit/random-product")
    fun getRandomProduit(): Call<Produit>

    @GET("produit/detail")
    fun getProduitDetails(@Query("id")id:String) :Call<Produit>

    @GET("produit")
    fun getPopularProduit(): Call<List<Produit>>
    @GET("produit")
    fun getProduit(): Call<ProduitList>

}