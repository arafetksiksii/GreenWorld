package tn.esprit.greenworld.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.greenworld.models.Dechets
import tn.esprit.greenworld.models.DechetsList
import tn.esprit.greenworld.utils.RetrofitInstance

class dechetviewmodel  : ViewModel() {
    private var listDechetLiveData = MutableLiveData<List<Dechets>>()

    fun getDechets(userId: String) {
        Log.d(" DechetViewModel", "Fetching dechets data")
        Log.d(" DechetViewModel", "Fetching dechets data for user $userId")

        RetrofitInstance.dechetapi.getDechetsForUser(userId).enqueue(object :
            Callback<DechetsList> {
            override fun onResponse(call: Call<DechetsList>, response: Response<DechetsList>) {
                if (response.isSuccessful) {
                    response.body()?.let { dechetsList ->
                        listDechetLiveData.postValue(dechetsList)
                        Log.d("DechetViewModel", "Received ${dechetsList.size} Types")
                    } ?: Log.d("DechetViewModel", "Response body is null")
                } else {
                    Log.d("DechetViewModel", "API response was not successful")
                }
            }


            override fun onFailure(call: Call<DechetsList>, t: Throwable) {
                Log.e("DechetViewModel", "API call failed with error: ${t.message}")
            }
        })
    }

    fun observeListDechetLiveData(): LiveData<List<Dechets>> {
        return listDechetLiveData

    }






    fun deleteDechets(id: String?) {
        if (id.isNullOrEmpty()) {
            Log.e("DechetsViewModel", "Error deleting dechets: ID is null or empty")
            return
        }

        val call = RetrofitInstance.dechetapi.deleteDechets(id)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    // Handle successful deletion
                    // You may want to refresh the list or update UI accordingly
                } else {
                    // Handle unsuccessful deletion
                    Log.e("DechetsViewModel", "Error deleting dechets: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Handle network failure
                Log.e("DechetsViewModel", "Network error: ${t.message}")
            }
        })
    }
}