package tn.esprit.greenworld.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.greenworld.R
import tn.esprit.greenworld.models.Favproduit
import tn.esprit.greenworld.models.Produit
import tn.esprit.greenworld.utils.RetrofitInstance

class FavProduitAdapter(private val service: RetrofitInstance) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var favProduit: Favproduit? = null

    private val VIEW_TYPE_PRODUCT = 0

    fun setFavProduit(favProduit: Favproduit) {
        this.favProduit = favProduit
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_PRODUCT -> {
                val itemView = LayoutInflater.from(parent.context).inflate(
                    R.layout.item_favproduit,
                    parent,
                    false
                )
                FavProduitViewHolder(itemView)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            VIEW_TYPE_PRODUCT -> {
                favProduit?.selectedProducts?.get(position)?.let {
                    (holder as FavProduitViewHolder).bind(it)
                    holder.bindRemoveButton(service, position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return favProduit?.selectedProducts?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE_PRODUCT
    }

    inner class FavProduitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.tvFavproduit)
        private val priceTextView: TextView = itemView.findViewById(R.id.tvPriceF)
        private val imageView: ImageView = itemView.findViewById(R.id.imgFavproduit)
        private val removeButton: Button = itemView.findViewById(R.id.btnRemoveFromFavProduit)

        fun bind(produit: Produit) {
            titleTextView.text = produit.title
            priceTextView.text = "Price: ${produit.price}TND"

            Glide.with(itemView.context)
                .load(produit.image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView)
        }

        fun bindRemoveButton(service: RetrofitInstance, position: Int) {
            removeButton.setOnClickListener {
                val selectedProduct = favProduit?.selectedProducts?.get(position)
                selectedProduct?.let {
                    val produitId = it.produit

                    service.Commandeapi.deleteProduitFromFav(produitId).enqueue(object : Callback<Favproduit> {
                        override fun onResponse(call: Call<Favproduit>, response: Response<Favproduit>) {
                            if (response.isSuccessful) {
                                val updatedProducts = favProduit?.selectedProducts?.toMutableList()
                                updatedProducts?.removeAt(position)
                                favProduit?.selectedProducts = updatedProducts ?: emptyList()
                                notifyDataSetChanged()
                            } else {
                                Log.e("FavProduitAdapter", "Failed to delete product from favorites")
                            }
                        }

                        override fun onFailure(call: Call<Favproduit>, t: Throwable) {
                            Log.e("FavProduitAdapter", "Failed to delete product from favorites", t)
                        }
                    })
                }
            }
        }
    }
}
