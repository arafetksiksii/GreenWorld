package tn.esprit.green_world.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import tn.esprit.green_world.databinding.ProductItemBinding
import tn.esprit.green_world.models.Produit
import tn.esprit.green_world.models.ProduitList

class ProduitListAdapter() : RecyclerView.Adapter<ProduitListAdapter.ProduitListViewHolder>() {
    private var produitsList = ArrayList<Produit>()

    fun setProduitList(produitList: List<Produit>) {
        this.produitsList = produitList as ArrayList<Produit>
        notifyDataSetChanged()
        Log.d("ProduitListAdapter", "setProduitList called with ${produitList.size} products") // Add this log
    }



    inner class ProduitListViewHolder(val binding: ProductItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProduitListViewHolder {
        return ProduitListViewHolder(
            ProductItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: ProduitListViewHolder, position: Int) {
        Glide.with(holder.itemView).load(produitsList[position].image).into(holder.binding.imgProduit)
        holder.binding.tvProductName.text= produitsList[position].title

    }

    override fun getItemCount(): Int {
        return produitsList.size
    }

}