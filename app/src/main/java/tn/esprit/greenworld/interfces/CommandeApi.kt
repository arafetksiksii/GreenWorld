package tn.esprit.greenworld.interfaces

import com.google.gson.annotations.SerializedName
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import tn.esprit.greenworld.models.Commande
import tn.esprit.greenworld.models.Favproduit
import tn.esprit.greenworld.models.Produit

interface CommandeApi {

    data class PaymentIntentResponse(
        @SerializedName("clientSecret") val clientSecret: String
    )
    @POST("commande/add-products")
    fun addProductToCart(@Query("produitId")produitId: String): Call<Commande>
    @GET("commande/cart")
    fun getCommande(): Call<Commande>
    @DELETE("commande/delete-product")
    fun deleteProduitFromCommande(@Query("produitId") produitId: String): Call<Commande>
    @POST("favproduit/add-products")
    fun addProductToFav(@Query("produitId")produitId: String): Call<Favproduit>
    @GET("favproduit/favorites")
    fun getProduitFav(): Call<Favproduit>
    @DELETE("favproduit/delete-product")
    fun deleteProduitFromFav(@Query("produitId") produitId: String): Call<Favproduit>
    @POST("commande/create-payment-intent")
    fun createPaymentIntent(): Call<PaymentIntentResponse>



}
