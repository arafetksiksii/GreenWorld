package com.example.testtt.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testtt.databinding.TypeItemBinding
import com.example.testtt.models.Type

class typeAdapter(): RecyclerView.Adapter<typeAdapter.TypeListViewHolder>() {
    private var typeList = ArrayList<Type>()
    lateinit var onListTypeClicked:((Type)-> Unit)


    fun setTypeList(typeList: List<Type>) {
        this.typeList = typeList as ArrayList<Type>
        notifyDataSetChanged()
        Log.d("TypeListAdapter", "setTypeList called with ${typeList.size} Types")
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeListViewHolder {
        return TypeListViewHolder(
            TypeItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun getItemCount(): Int {
        return typeList.size
    }

    override fun onBindViewHolder(holder: TypeListViewHolder, position: Int) {
        val type = typeList[position]
        Glide.with(holder.binding.root)
            .load(type.image_type)
            .into(holder.binding.image)
        holder.binding.titre.text= typeList[position].titre
        holder.itemView.setOnClickListener {
            onListTypeClicked.invoke(typeList[position])

        }
    }

    inner class TypeListViewHolder(val binding: TypeItemBinding):RecyclerView.ViewHolder(binding.root)

}