package tn.esprit.green_world.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.green_world.models.Produit
import tn.esprit.green_world.models.ProduitItem
import tn.esprit.green_world.models.ProduitList
import tn.esprit.green_world.utils.RetrofitInstance

class ProduitViewModel : ViewModel() {
    private var randomProduitLiveData = MutableLiveData<Produit>()
    private var listProduitLiveData = MutableLiveData<List<Produit>>()
    private var popularProduitLiveData = MutableLiveData<List<Produit>?>() // Updated to hold a list of Produit


    fun getRandomProduit() {
        RetrofitInstance.api.getRandomProduit().enqueue(object : Callback<Produit> {
            override fun onResponse(call: Call<Produit>, response: Response<Produit>) {
                if (response.isSuccessful) {
                    val produit: Produit? = response.body()
                    produit?.let {
                        val productId = it._id
                        val productTitle = it.title
                        randomProduitLiveData.value = it
                        Log.d("ProduitFragment", "Random Product ID: $productId")
                        Log.d("ProduitFragment", "Random Product Title: $productTitle")
                    } ?: run {
                        Log.d("ProduitFragment", "Random API response body is null")
                    }
                } else {
                    Log.d("ProduitFragment", "Random API call was not successful")
                }
            }

            override fun onFailure(call: Call<Produit>, t: Throwable) {
                Log.e("ProduitFragment", "Random API call failed: ${t.message}")
            }
        })
    }

    fun getPopularProduit() {
        RetrofitInstance.api.getRandomProduit().enqueue(object : Callback<Produit> {
            override fun onResponse(call: Call<Produit>, response: Response<Produit>) {
                if (response.isSuccessful) {
                    val produit: Produit? = response.body()
                    produit?.let {
                        val productId = it._id
                        val productTitle = it.title

                        popularProduitLiveData.value = listOf(it)
                        Log.d("ProduitFragment", "Random Product ID: $productId")
                        Log.d("ProduitFragment", "Random Product Title: $productTitle")
                    } ?: run {
                        Log.d("ProduitFragment", "Random API response body is null")
                    }
                } else {
                    Log.d("ProduitFragment", "Random API call was not successful")
                }
            }

            override fun onFailure(call: Call<Produit>, t: Throwable) {
                Log.e("ProduitFragment", "Random API call failed: ${t.message}")
            }
        })
    }

    fun getProducts() {
        Log.d("ProduitViewModel", "Fetching products data")
        RetrofitInstance.api.getProduit().enqueue(object : Callback<ProduitList> {
            override fun onResponse(call: Call<ProduitList>, response: Response<ProduitList>) {
                if (response.isSuccessful) {
                    response.body()?.let { produitList ->
                        listProduitLiveData.postValue(produitList)
                        Log.d("ProduitViewModel", "Received ${produitList.size} products")
                    } ?: Log.d("ProduitViewModel", "Response body is null")
                } else {
                    Log.d("ProduitViewModel", "API response was not successful")
                }
            }

            override fun onFailure(call: Call<ProduitList>, t: Throwable) {
                Log.e("ProduitViewModel", "API call failed with error: ${t.message}")
            }
        })
    }



    fun observeRandomProduitLiveData(): MutableLiveData<Produit> {
        return randomProduitLiveData
    }

    fun observePopularProduitLiveData(): MutableLiveData<List<Produit>?> {
        return popularProduitLiveData
    }
    fun observeListProduitLiveData(): LiveData<List<Produit>>{
        return listProduitLiveData

    }
}
