package com.example.testtt.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testtt.databinding.EventItemBinding
import com.example.testtt.models.Event

class EventAdapter (): RecyclerView.Adapter<EventAdapter.EventListViewHolder>() {
    private var eventList = ArrayList<Event>()
    lateinit var onListEventClicked:((Event)-> Unit)

    fun setEventList(EventList: List<Event>) {

        this.eventList = EventList as ArrayList<Event>
        notifyDataSetChanged()
        Log.d("EventListAdapter", "setEventList called with ${eventList.size} Event")
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventAdapter.EventListViewHolder {
        return EventListViewHolder(
            EventItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: EventListViewHolder, position: Int) {
        val event = eventList[position]
        Glide.with(holder.binding.root)
            .load(event.image)
            .into(holder.binding.imageEvent)
        holder.binding.titreEvent.text= eventList[position].titre
        holder.itemView.setOnClickListener {
            onListEventClicked.invoke(eventList[position])

        }    }

    override fun getItemCount(): Int {
        return eventList.size
    }


    inner class EventListViewHolder(val binding: EventItemBinding):RecyclerView.ViewHolder(binding.root)

}