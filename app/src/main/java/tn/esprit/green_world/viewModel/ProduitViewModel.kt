package tn.esprit.green_world.viewModel
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.green_world.models.Produit
import tn.esprit.green_world.utils.RetrofitInstance

class ProduitViewModel():ViewModel() {

    private var randomProduitLiveData = MutableLiveData<Produit>()

    fun getRandomProduit() {
        RetrofitInstance.api.getRandomProduit().enqueue(object : Callback<Produit> {
            override fun onResponse(call: Call<Produit>, response: Response<Produit>) {
                if (response.isSuccessful) {
                    val produit: Produit? = response.body()

                    produit?.let {
                        // The produit is not null, so you can safely access its properties.
                        val productId = it._id
                        val productTitle = it.title

                        randomProduitLiveData.value = it

                        // Log the information to see it in the Logcat
                        Log.d("ProduitFragment", "Product ID: $productId")
                        Log.d("ProduitFragment", "Product Title: $productTitle")
                        // and so on for other properties
                    } ?: run {
                        // Handle the case where the produit is null
                        Log.d("ProduitFragment", "API response body is null")
                    }
                } else {
                    Log.d("ProduitFragment", "API call was not successful")
                }
            }

            override fun onFailure(call: Call<Produit>, t: Throwable) {
                Log.e("ProduitFragment", "API call failed: ${t.message}")
            }
        })
    }

    fun observeRandomProduitLiveData():MutableLiveData<Produit>{
        return randomProduitLiveData
    }

}