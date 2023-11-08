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
import java.util.Objects

class ProduitDetailViewModel(): ViewModel() {
    private var produitDetailLiveData = MutableLiveData<Produit>()

    fun getProduitDetail(id: String) {
        RetrofitInstance.api.getProduitDetails(id).enqueue(object : Callback<Produit> {
            override fun onResponse(call: Call<Produit>, response: Response<Produit>) {
                if (response.body() != null) {
                    val produit = response.body()!!
                    Log.d("ProduitDetailViewModel", "Received product: $produit")
                    produitDetailLiveData.value = produit
                } else {
                    Log.d("ProduitDetailViewModel", "Response body is null")
                }
            }

            override fun onFailure(call: Call<Produit>, t: Throwable) {
                Log.e("ProduitDetailViewModel", "Request failed: ${t.message}")
            }
        })
    }

   fun obersverProduitDetailLiveData():LiveData<Produit>{
       return produitDetailLiveData
   }
}