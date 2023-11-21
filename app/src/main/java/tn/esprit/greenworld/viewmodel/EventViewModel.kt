package tn.esprit.greenworld.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tn.esprit.greenworld.models.Comment
import tn.esprit.greenworld.models.Event
import tn.esprit.greenworld.models.EventList

import tn.esprit.greenworld.API.CommentApi
import tn.esprit.greenworld.API.EventApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.greenworld.utils.RetrofitInstance

class EventViewModel() : ViewModel() {
    private var listEventLiveData = MutableLiveData<List<Event>>()
    private val commentListLiveData   = MutableLiveData<List<Comment>>()
    fun getEvent() {
        Log.d("EventViewModel", "Fetching Event data")
        RetrofitInstance.apiEvent.getEvent().enqueue(object : Callback<EventList> {

            override fun onResponse(call: Call<EventList>, response: Response<EventList>) {
                if (response.isSuccessful) {
                    response.body()?.let { eventlist ->
                        listEventLiveData.postValue(eventlist)
                        Log.d("EventViewModel", "Received ${eventlist.size} Events")
                    } ?: Log.d("EventViewModel", "Response body is null")
                } else {
                    Log.d("EventViewModel", "API response was not successful")
                }
            }

            override fun onFailure(call: Call<EventList>, t: Throwable) {
                Log.e("EventViewModel", "API call failed with error: ${t.message}")
            }

        })
    }


    fun observeListEventLiveData(): LiveData<List<Event>> {
        return listEventLiveData

    }

}