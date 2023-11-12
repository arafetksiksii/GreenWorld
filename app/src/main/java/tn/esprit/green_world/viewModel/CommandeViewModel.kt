package tn.esprit.green_world.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import tn.esprit.green_world.models.Commande
import tn.esprit.green_world.repositories.CommandeRepository

class CommandeViewModel : ViewModel() {
    private val repository = CommandeRepository()

    // Assuming userId is set on the server side, no need to pass it as a parameter
    fun getCommande(): LiveData<Commande?> {
        return repository.getCommande()
    }
}