package tn.esprit.greenworld.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import tn.esprit.greenworld.models.Commande
import tn.esprit.greenworld.repositories.CommandeRepository

class CommandeViewModel : ViewModel() {
    private val repository = CommandeRepository()

    fun getCommande(): LiveData<Commande?> {
        return repository.getCommande()
    }
}