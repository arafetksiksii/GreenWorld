package tn.esprit.green_world.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Commande(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    @SerializedName("selectedProducts")
    var selectedProducts: List<Produit>,
    val userId: String,
    val totalPrice: String,
    val updatedAt: String,
    val paymentMethodId: String?,
    @SerializedName("client_secret")
    val clientSecret: String? = null

) : Serializable