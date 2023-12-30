package tn.esprit.greenworld.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tn.esprit.greenworld.models.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.greenworld.utils.RetrofitInstance

class EventDetailsViewModel (): ViewModel() {
    private var eventDetailLiveData = MutableLiveData<Event>()

   fun getEventDetail(id: String) {
        RetrofitInstance.apiEvent.getEventDetails(id).enqueue(object : Callback<Event> {
            override fun onResponse(call: Call<Event>, response: Response<Event>) {
                if (response.body() != null) {
                    val event = response.body()!!
                    Log.d("test", "Received event: $event")
                    eventDetailLiveData.value = event
                } else {
                    Log.d("test", "Response body is null")
                }
            }

            override fun onFailure(call: Call<Event>, t: Throwable) {
                Log.e("test", "Request failed: ${t.message}")
            }
        })


    }


    fun obersverEventDetailLiveData(): LiveData<Event> {
        return eventDetailLiveData
    }
}