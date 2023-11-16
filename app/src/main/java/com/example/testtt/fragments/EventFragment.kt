package com.example.testtt.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.testtt.R
import com.example.testtt.adapters.EventAdapter
import com.example.testtt.databinding.FragmentEventBinding
import com.example.testtt.models.Event
import com.example.testtt.viewModel.EventViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [EventFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EventFragment : Fragment() {

    private lateinit var binding: FragmentEventBinding
    private lateinit var eventMvvm: EventViewModel
    private lateinit var eventListAdapter: EventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        eventMvvm = ViewModelProvider(this).get(EventViewModel::class.java)
        eventListAdapter = EventAdapter()

    }

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        const val event_id = "com.example.testtt.fragments.idevent"
        const val event_titre = "com.example.testtt.fragments.titre_event"
        const val event_image= "com.example.testtt.fragments.image_event"


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareListEventRecyclerView()
        eventMvvm.getEvent()
        observeListEventLiveData()
        onListEventClicked()

        prepareListEventRecyclerView()
    }
    private fun prepareListEventRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = eventListAdapter
        }
        Log.d("EventFragment", "RecyclerView prepared")
    }
    private fun observeListEventLiveData() {
        // Dans votre fragment
        eventMvvm.observeListEventLiveData().observe(viewLifecycleOwner, Observer { event ->
            event?.let {
                Log.d("EventFragment", "Received ${event.size} events")
                eventListAdapter.setEventList(event)
                Log.d("EventFragment", "Adapter updated")
            } ?: run {
                Log.d("EventFragment", "List API response body is null")
            }
        })

    }
    private fun onListEventClicked() {
        eventListAdapter.onListEventClicked = { event ->
            Log.d("EventFragment", " Event Clicked: ${event.image}")
            Log.d("EventFragment", "Event ID: ${event._id}")
            Log.d("EventFragment", "Event Title: ${event.titre}")

            val intent = Intent(activity, Event::class.java)
          intent.putExtra(event._id, event._id)
           intent.putExtra(event.titre, event.titre)
            intent.putExtra(event.image, event.image)
            startActivity(intent)
        }
    }
}