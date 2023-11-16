package com.example.testtt.adapters
import com.bumptech.glide.request.target.Target

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
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
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.e("Glide", "Échec du chargement de l'image: $e")
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            })
            .into(holder.binding.imageEvent)


        println(event)
        holder.binding.titreEvent.text = event.titre
        holder.binding.location.text=event.lieu
        holder.binding.dateDebut.text=event.datedebut
        holder.binding.nbParticipant.text= event.nbparticipant.toString()
        holder.itemView.setOnClickListener {
            onListEventClicked.invoke(event)
        }

        holder.binding.titreEvent.text= eventList[position].titre
        holder.binding.location.text=eventList[position].lieu

        holder.binding.dateDebut.text=eventList[position].datedebut
        holder.binding.nbParticipant.text= eventList[position].nbparticipant.toString()
        holder.itemView.setOnClickListener {
            onListEventClicked.invoke(eventList[position])

        }    }

    override fun getItemCount(): Int {
        return eventList.size
    }


    inner class EventListViewHolder(val binding: EventItemBinding):RecyclerView.ViewHolder(binding.root)

}