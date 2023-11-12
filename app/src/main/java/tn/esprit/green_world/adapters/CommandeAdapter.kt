package tn.esprit.green_world.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import tn.esprit.green_world.R
import tn.esprit.green_world.models.Commande
import tn.esprit.green_world.models.Produit

// ... (imports)

class CommandeAdapter : RecyclerView.Adapter<CommandeAdapter.CommandeViewHolder>() {

    private var commande: Commande? = null

    fun setCommande(commande: Commande) {
        this.commande = commande
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommandeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_commande,
            parent,
            false
        )
        return CommandeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CommandeViewHolder, position: Int) {
        commande?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int {
        return commande?.selectedProducts?.size ?: 0
    }

    inner class CommandeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(commande: Commande) {
            val selectedProduct = commande.selectedProducts[adapterPosition] as Produit

            // Bind data to the product view
            val titleTextView = itemView.findViewById<TextView>(R.id.tvCommande)
            val priceTextView = itemView.findViewById<TextView>(R.id.tvPriceC)
            val quantityTextView = itemView.findViewById<TextView>(R.id.tvQuantityC)
            val imageView = itemView.findViewById<ImageView>(R.id.imgCommande)

            titleTextView.text = selectedProduct.title
            priceTextView.text = "Price: ${selectedProduct.price}"
            quantityTextView.text = "Quantity: ${selectedProduct.quantity}"

            Glide.with(itemView.context)
                .load(selectedProduct.image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView)
        }
    }
}
