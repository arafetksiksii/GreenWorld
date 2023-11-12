package tn.esprit.green_world.models

data class Commande(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val selectedProducts: List<Produit>,
    val userId: String,
    val totalPrice: String,
    val updatedAt: String
)