package tn.esprit.green_world.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.green_world.interfaces.CommandeApi
import tn.esprit.green_world.models.Commande
import tn.esprit.green_world.utils.RetrofitInstance

class CommandeRepository {
    private val api: CommandeApi = RetrofitInstance.Commandeapi


    // Function to get a single commande for a specific user (hardcoded for now)
    fun getCommande(): LiveData<Commande?> {
        val commandeLiveData = MutableLiveData<Commande?>()

        // Make the network request to get a single commande for the user
        api.getCommande().enqueue(object : Callback<Commande> {
            override fun onResponse(call: Call<Commande>, response: Response<Commande>) {
                if (response.isSuccessful) {
                    // If the response is successful, update the LiveData with the data
                    val commande = response.body()
                    commandeLiveData.value = commande
                } else {
                    // Handle unsuccessful response
                    // You can parse the error response using response.errorBody()
                    // For now, just set null to LiveData
                    commandeLiveData.value = null
                }
            }

            override fun onFailure(call: Call<Commande>, t: Throwable) {
                // Handle failure
                // For now, just set null to LiveData
                commandeLiveData.value = null
            }
        })

        return commandeLiveData
    }

    fun deleteProduct(productId: String) {
        val call: Call<Commande> = api.deleteProduitFromCommande(productId)

        call.enqueue(object : Callback<Commande> {
            override fun onResponse(call: Call<Commande>, response: Response<Commande>) {
                // Handle successful response if needed
            }

            override fun onFailure(call: Call<Commande>, t: Throwable) {
                // Handle failure
            }
        })
    }
    // Add other repository methods as needed, for example, a method to add a product to the cart
}
