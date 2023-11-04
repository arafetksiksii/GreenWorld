package tn.esprit.greenworld.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import tn.esprit.greenworld.models.Produit



@Dao
interface ProduitDao {

    @Insert
    fun insertProduit(produit: Produit)

    @Delete
    fun deleteProduit(produit: Produit)

    @Update
    fun updateProduit(produit: Produit)

    @Query("select * from produit")
    fun getAllProduit(): MutableList<Produit>

    @Query("select * from produit where id = :id")
    fun getProduitById(id: Int): Produit?

}