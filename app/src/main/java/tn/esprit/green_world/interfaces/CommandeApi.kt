package tn.esprit.green_world.interfaces

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import tn.esprit.green_world.models.Commande
import tn.esprit.green_world.models.Produit

interface CommandeApi {

    @POST("commande/add-products")
    fun addProductToCart(@Query("produitId")produitId: String): Call<Commande>
    @GET("commande/cart")
    fun getCommande(): Call<Commande>

}
