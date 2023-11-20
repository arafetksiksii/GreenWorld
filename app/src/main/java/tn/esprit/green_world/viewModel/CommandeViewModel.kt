package tn.esprit.green_world.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import tn.esprit.green_world.models.Commande
import tn.esprit.green_world.repositories.CommandeRepository

class CommandeViewModel : ViewModel() {
    private val repository = CommandeRepository()

    fun getCommande(): LiveData<Commande?> {
        return repository.getCommande()
    }
}