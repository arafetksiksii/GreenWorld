package tn.esprit.green_world.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import tn.esprit.green_world.models.Favproduit
import tn.esprit.green_world.repositories.FavProduitRepository

class FavProduitViewModel : ViewModel() {
    private val repository = FavProduitRepository()

    fun getFavproduit(): LiveData<Favproduit?> {
        return repository.getProduitFav()
    }
}
