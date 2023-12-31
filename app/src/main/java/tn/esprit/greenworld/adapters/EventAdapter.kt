package tn.esprit.greenworld.adapters
import android.content.Context

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import tn.esprit.greenworld.databinding.EventItemBinding
import tn.esprit.greenworld.models.Event
import com.squareup.picasso.Picasso

class EventAdapter (): RecyclerView.Adapter<EventAdapter.EventListViewHolder>() {
    private var eventList = ArrayList<Event>()
     var onItemClick:((Event)-> Unit)? = null
    var onButtonSheet:((Event)-> Unit)? = null
    lateinit var onListEventClick:((Event)-> Unit)



    fun setEventList(EventList: List<Event>) {

        this.eventList = EventList as ArrayList<Event>
        notifyDataSetChanged()
        Log.d("EventListAdapter", "setEventList called with ${eventList.size} Event")
    }

    // Define an interface for item click listener
    interface OnItemClickListener {
        fun onItemClick(event: Event?)
    }

    // Member variable to store the listener
    private var onItemClickListener: OnItemClickListener? = null

    // Setter for the listener
    fun setOnItemClickListener(listener: OnItemClickListener?) {
        onItemClickListener = listener
    }

    // Call this method when an item is clicked
    private fun notifyItemClickListener(event: Event) {
        if (onItemClickListener != null) {
            onItemClickListener!!.onItemClick(event)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventAdapter.EventListViewHolder {
        return EventListViewHolder(
            EventItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: EventListViewHolder, position: Int) {
        val event = eventList[position]
        holder.binding.commenter.setOnClickListener {
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onItemClick?.invoke(eventList[position])
            }
        }
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onItemClick?.invoke(eventList[position])
            }
        }
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onButtonSheet?.invoke(eventList[position])
            }
        }



        println(event)
        val imageUrl = "http://10.0.2.2:9090/img/${event.image}"
        Log.d("Glide", "Image URL: $imageUrl")

        Glide.with(holder.binding.root)
            .load(imageUrl)
            .into(holder.binding.imageEvent)
      //  holder.binding.titreEvent.text = event.titre
      //  holder.binding.location.text=event.lieu
    //    holder.binding.dateDebut.text=event.datedebut

      //  holder.binding.nbParticipant.text= event.nbparticipant.toString()
        holder.binding.titreEvent.text= eventList[position].titre
        holder.binding.location.text=eventList[position].lieu
        // holder.binding.dateDebut.text= eventList[position].datedebut.toString()
        holder.binding.nbParticipant.text= eventList[position].nbparticipant.toString()
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(eventList[position])

        }
        holder.itemView.setOnClickListener {
            onListEventClick.invoke(eventList[position])

        }
    }

    override fun getItemCount(): Int {
        return eventList.size
    }


    inner class EventListViewHolder(val binding: EventItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }

}