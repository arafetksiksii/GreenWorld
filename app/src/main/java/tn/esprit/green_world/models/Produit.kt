package tn.esprit.green_world.models

import android.os.Parcelable
import java.io.Serializable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Produit(
    val __v: Int,
    val _id: String,
    val produit: String,
    val createdAt: String,
    val description: String,
    val image: String,
    val price: Int,
    val quantity: Int,
    val title: String,
    val updatedAt: String
) : Serializable, Parcelable