package tn.esprit.greenworld.adapters

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import tn.esprit.greenworld.databinding.DechetsItemBinding
import tn.esprit.greenworld.models.Dechets
import tn.esprit.greenworld.models.Type
import com.bumptech.glide.request.target.Target
import tn.esprit.greenworld.R

class dechetsAdapter(

    ): RecyclerView.Adapter<dechetsAdapter.DechetsListViewHolder>() {
    private var dechetsList = ArrayList<Dechets>()
    lateinit var onDeleteButtonClicked: ((Dechets) -> Unit)

    lateinit var onListDechetsClicked:((Dechets)-> Unit)
    fun setdechetList(dechetsList: List<Dechets>) {
        this.dechetsList = dechetsList as ArrayList<Dechets>
        notifyDataSetChanged()
        Log.d("DechetsListAdapter", "setTypeList called with ${dechetsList.size} Dechets")
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DechetsListViewHolder {
        return DechetsListViewHolder(
            DechetsItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun getItemCount(): Int {
        return dechetsList.size
    }

    override fun onBindViewHolder(holder: DechetsListViewHolder, position: Int) {

        holder.binding.delete.setOnClickListener {
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onDeleteButtonClicked.invoke(dechetsList[position])
            }
        }


        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onListDechetsClicked?.invoke(dechetsList[position])
            }
        }

        holder.binding.typed.text = dechetsList[position].Type_dechets
        holder.binding.adresee.text = dechetsList[position].adresse
        holder.binding.dateDepot.text = dechetsList[position].date_depot
        holder.binding.capacite.text = dechetsList[position].nombre_capacite.toString()

        holder.itemView.setOnClickListener {
            onListDechetsClicked.invoke(dechetsList[position])
        }
    }
    inner class DechetsListViewHolder(val binding: DechetsItemBinding): RecyclerView.ViewHolder(binding.root)


    }

