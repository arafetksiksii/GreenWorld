package tn.esprit.green_world.adapters

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
import tn.esprit.green_world.R
import tn.esprit.green_world.models.Commande
import tn.esprit.green_world.models.Produit
import tn.esprit.green_world.utils.RetrofitInstance

class CommandeAdapter(private val service: RetrofitInstance) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var commande: Commande? = null
    private var totalPrice: String = "0"

    private val VIEW_TYPE_PRODUCT = 0
    private val VIEW_TYPE_TOTAL = 1

    fun setCommande(commande: Commande) {
        this.commande = commande
        notifyDataSetChanged()
    }

    fun setTotalPrice(totalPrice: String) {
        this.totalPrice = totalPrice
        Log.d("CommandeAdapter", "Setting total price: $totalPrice")

        notifyDataSetChanged()
    }
    fun updateTotalPrice(newTotalPrice: String) {
        this.totalPrice = newTotalPrice
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_PRODUCT -> {
                val itemView = LayoutInflater.from(parent.context).inflate(
                    R.layout.item_commande,
                    parent,
                    false
                )
                CommandeViewHolder(itemView)
            }
            VIEW_TYPE_TOTAL -> {
                val totalView = LayoutInflater.from(parent.context).inflate(
                    R.layout.item_total_price,
                    parent,
                    false
                )
                TotalPriceViewHolder(totalView)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            VIEW_TYPE_PRODUCT -> {
                commande?.selectedProducts?.get(position)?.let {
                    (holder as CommandeViewHolder).bind(it)
                    holder.bindRemoveButton(service, position)
                }
            }
            VIEW_TYPE_TOTAL -> {
                (holder as TotalPriceViewHolder).bindTotalPrice(totalPrice)
            }
        }
    }

    override fun getItemCount(): Int {
        return (commande?.selectedProducts?.size ?: 0) + 1 // +1 for the total price item
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < (commande?.selectedProducts?.size ?: 0)) {
            VIEW_TYPE_PRODUCT
        } else {
            VIEW_TYPE_TOTAL
        }
    }

    inner class CommandeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.tvCommande)
        private val priceTextView: TextView = itemView.findViewById(R.id.tvPriceC)
        private val quantityTextView: TextView = itemView.findViewById(R.id.tvQuantityC)
        private val imageView: ImageView = itemView.findViewById(R.id.imgCommande)
        private val removeButton: Button = itemView.findViewById(R.id.btnRemoveFromCart)

        fun bind(produit: Produit) {
            titleTextView.text = produit.title
            priceTextView.text = "Price: ${produit.price}TND"
            quantityTextView.text = "Quantity: ${produit.quantity}"

            Glide.with(itemView.context)
                .load(produit.image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView)
        }

        fun bindRemoveButton(service: RetrofitInstance, position: Int) {
            removeButton.setOnClickListener {
                val selectedProduct = commande?.selectedProducts?.get(position)
                selectedProduct?.let {
                    val produitId = it.produit

                    service.Commandeapi.deleteProduitFromCommande(produitId).enqueue(object : Callback<Commande> {
                        override fun onResponse(call: Call<Commande>, response: Response<Commande>) {
                            if (response.isSuccessful) {
                                val updatedProducts = commande?.selectedProducts?.toMutableList()
                                updatedProducts?.removeAt(position)
                                commande?.selectedProducts = updatedProducts ?: emptyList()
                                updateTotalPrice(totalPrice)
                                notifyDataSetChanged()
                            } else {
                                Log.e("CommandeAdapter", "Failed to delete product from cart")
                            }
                        }

                        override fun onFailure(call: Call<Commande>, t: Throwable) {
                            Log.e("CommandeAdapter", "Failed to delete product from cart", t)
                        }
                    })
                }
            }
        }
    }
    inner class TotalPriceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val totalPriceTextView: TextView = itemView.findViewById(R.id.tvTotalPrice)

        fun bindTotalPrice(totalPrice: String) {
            Log.d("TotalPriceViewHolder", "Binding total price: $totalPrice")
            totalPriceTextView.text = "Total: $totalPrice TND"
        }
    }

}
