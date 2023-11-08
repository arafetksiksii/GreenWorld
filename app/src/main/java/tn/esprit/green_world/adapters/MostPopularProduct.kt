package tn.esprit.green_world.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tn.esprit.green_world.databinding.PopularProduitBinding
import tn.esprit.green_world.models.Produit

class MostPopularProduct : RecyclerView.Adapter<MostPopularProduct.PopularProduitViewHolder>() {
    lateinit var onMostpopularProductClick:((Produit)-> Unit)
    private var produitList = ArrayList<Produit>()

    fun setProduit(produitList: List<Produit>) { // Updated to accept a list of Produit
        this.produitList = ArrayList(produitList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularProduitViewHolder {
        return PopularProduitViewHolder(
            PopularProduitBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PopularProduitViewHolder, position: Int) {
        val produit = produitList[position]
        Glide.with(holder.itemView)
            .load(produit.image)
            .into(holder.binding.popularProductImage)
        holder.itemView.setOnClickListener{
            onMostpopularProductClick.invoke(produitList[position])
        }
    }

    override fun getItemCount(): Int {
        return produitList.size
    }

    class PopularProduitViewHolder(val binding: PopularProduitBinding) : RecyclerView.ViewHolder(binding.root)
}
