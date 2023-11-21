package tn.esprit.greenworld.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import tn.esprit.greenworld.models.Favproduit
import tn.esprit.greenworld.repositories.FavProduitRepository

class FavProduitViewModel : ViewModel() {
    private val repository = FavProduitRepository()

    fun getFavproduit(): LiveData<Favproduit?> {
        return repository.getProduitFav()
    }
}
