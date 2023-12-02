package tn.esprit.greenworld.fragments
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
import tn.esprit.greenworld.UserUpdate
import tn.esprit.greenworld.activities.CustomCalendar
import tn.esprit.greenworld.activities.EventsMainPage
import tn.esprit.greenworld.adapters.EventAdapter
import tn.esprit.greenworld.databinding.FragmentEventBinding
import tn.esprit.greenworld.viewModel.EventViewModel
import tn.esprit.greenworld.activities.MapActivity
import tn.esprit.greenworld.ui.quiz_activity.QuizListActivity

import tn.esprit.greenworld.utils.RecycleViewEvent

class EventFragment : Fragment(), RecycleViewEvent {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareListEventRecyclerView()
        eventMvvm.getEvent()
        observeListEventLiveData()
        onListEventClicked()
        binding.map.setOnClickListener {
            val intent = Intent(requireContext(), MapActivity::class.java)
            startActivity(intent)
        }
        binding.calender.setOnClickListener {
            val intent = Intent(requireContext(), CustomCalendar::class.java)
            startActivity(intent)
        }

            binding.btnProfil.setOnClickListener {
                val intent = Intent(requireContext(), QuizListActivity::class.java)
                startActivity(intent)
            }


    }
    private fun prepareListEventRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            adapter = eventListAdapter
        }
        Log.d("EventFragment", "RecyclerView prepared")
    }

    private fun observeListEventLiveData() {
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
        eventListAdapter.onListEventClick = { event ->
            Log.d("EventFragment", " Event Clicked: ${event.image}")
            Log.d("EventFragment", "Event ID: ${event._id}")
            Log.d("EventFragment", "Event Title: ${event.titre}")
            Log.d("EventFragment", "Event Description: ${event.description}")
            Log.d("EventFragment", "Event Location: ${event.lieu}")
            Log.d("EventFragment", "Event nombre de participant: ${event.nbparticipant}")



            val intent = Intent(activity, EventsMainPage::class.java)
            intent.putExtra("event_id", event._id)
            intent.putExtra("event_titre", event.titre)
            intent.putExtra("event_image", event.image)
            intent.putExtra("event_location", event.lieu)
            intent.putExtra("event_description", event.description)
            intent.putExtra("event_nbParticipant", event.nbparticipant)


            startActivity(intent)
        }
    }

    override fun onItemClicked(position: Int) {
        // Handle item click if needed
    }
}
