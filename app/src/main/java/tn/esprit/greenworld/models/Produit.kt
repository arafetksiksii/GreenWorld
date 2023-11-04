package tn.esprit.greenworld.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Produit(

    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val imageRes: Int,

    val title: String,

    val description: String,

    val price: String,

    val quantity: String,

    )