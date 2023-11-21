package tn.esprit.greenworld.models

data class Favproduit(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    var selectedProducts: List<Produit>,
    val userId: String,
    val updatedAt: String
)