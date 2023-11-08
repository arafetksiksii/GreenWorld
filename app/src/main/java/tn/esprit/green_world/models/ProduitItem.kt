package tn.esprit.green_world.models

data class ProduitItem(
    val description: String,
    val id: String,
    val image: String,
    val price: Int,
    val quantity: Int,
    val title: String
)