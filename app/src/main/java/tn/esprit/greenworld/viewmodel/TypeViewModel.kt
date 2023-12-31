package tn.esprit.greenworld.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.greenworld.models.Type
import tn.esprit.greenworld.models.TypeList
import tn.esprit.greenworld.utils.RetrofitInstance


class TypeViewModel : ViewModel() {
    private var listTypeLiveData = MutableLiveData<List<Type>>()

    fun getType() {
        Log.d("TypeViewModel", "Fetching Type data")
        RetrofitInstance.apii.getType().enqueue(object : Callback<TypeList> {
            override fun onResponse(call: Call<TypeList>, response: Response<TypeList>) {
                if (response.isSuccessful) {
                    response.body()?.let { typeList ->
                        listTypeLiveData.postValue(typeList)
                        Log.d("TypeViewModel", "Received ${typeList.size} Types")
                    } ?: Log.d("TypeViewModel", "Response body is null")
                } else {
                    Log.d("TypeViewModel", "API response was not successful")
                }
            }



            override fun onFailure(call: Call<TypeList>, t: Throwable) {
                Log.e("TypeViewModel", "API call failed with error: ${t.message}")
            }
        })
    }

    fun observeListTypeLiveData(): LiveData<List<Type>> {
        return listTypeLiveData

    }

}