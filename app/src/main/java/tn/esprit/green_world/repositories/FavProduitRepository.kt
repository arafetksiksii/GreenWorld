package tn.esprit.green_world.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.green_world.interfaces.CommandeApi
import tn.esprit.green_world.models.Favproduit
import tn.esprit.green_world.utils.RetrofitInstance

class FavProduitRepository {
    private val api: CommandeApi = RetrofitInstance.Commandeapi

    // Function to get a single Favproduit for a specific user (hardcoded for now)
    fun getProduitFav(): LiveData<Favproduit?> {
        val favProduitLiveData = MutableLiveData<Favproduit?>()

        // Make the network request to get a single Favproduit for the user
        api.getProduitFav().enqueue(object : Callback<Favproduit> {
            override fun onResponse(call: Call<Favproduit>, response: Response<Favproduit>) {
                if (response.isSuccessful) {
                    // If the response is successful, update the LiveData with the data
                    val favProduit = response.body()
                    favProduitLiveData.value = favProduit
                } else {
                    // Handle unsuccessful response
                    // You can parse the error response using response.errorBody()
                    // For now, just set null to LiveData
                    favProduitLiveData.value = null
                }
            }

            override fun onFailure(call: Call<Favproduit>, t: Throwable) {
                // Handle failure
                // For now, just set null to LiveData
                favProduitLiveData.value = null
            }
        })

        return favProduitLiveData
    }

    fun deleteProduct(productId: String) {
        val call: Call<Favproduit> = api.deleteProduitFromFav(productId)

        call.enqueue(object : Callback<Favproduit> {
            override fun onResponse(call: Call<Favproduit>, response: Response<Favproduit>) {
                // Handle successful response if needed
            }

            override fun onFailure(call: Call<Favproduit>, t: Throwable) {
                // Handle failure
            }
        })
    }
    // Add other repository methods as needed, for example, a method to add a product to favorites
}
