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
    private var produitList = ArrayList<Produit>()
    lateinit var onListProductClick:((Produit)-> Unit)

    fun setProduitList(produitList: List<Produit>) {
        this.produitList = produitList as ArrayList<Produit>
        notifyDataSetChanged()
        Log.d("ProduitListAdapter", "setProduitList called with ${produitList.size} products")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProduitListViewHolder {
        return ProduitListViewHolder(
            ProductItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }
    override fun onBindViewHolder(holder: ProduitListViewHolder, position: Int) {
        val produit = produitList[position]
        Glide.with(holder.binding.root)
            .load(produit.image)
            .into(holder.binding.imgProduit)
        Log.d("produitimage", produit.image)
        holder.binding.tvProductName.text= produitList[position].title
        holder.itemView.setOnClickListener {
            onListProductClick.invoke(produitList[position])

        }
    }






    override fun getItemCount(): Int {
        return produitList.size
    }
    inner class ProduitListViewHolder(val binding: ProductItemBinding):RecyclerView.ViewHolder(binding.root)
}