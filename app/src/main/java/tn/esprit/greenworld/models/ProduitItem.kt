package tn.esprit.greenworld.models

data class ProduitItem(
    val description: String,
    val id: String,
    val image: String,
    val price: Int,
    val quantity: Int,
    val title: String
)