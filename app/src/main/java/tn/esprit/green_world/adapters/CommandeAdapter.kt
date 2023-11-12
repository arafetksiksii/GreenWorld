package tn.esprit.green_world.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import tn.esprit.green_world.R
import tn.esprit.green_world.models.Commande
import tn.esprit.green_world.models.Produit

// ... (existing imports)

// ... (existing imports)

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
        return 1  // Only one Commande instance is displayed
    }

    inner class CommandeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(commande: Commande) {
            // Get the list of selected products
            val selectedProducts = commande.selectedProducts

            // Check if the list is not empty
            if (selectedProducts.isNotEmpty()) {
                // Bind data to views for each product
                for (selectedProduct in selectedProducts) {
                    // Extract information from the product
                    val product = selectedProduct as Produit // Assuming 'selectedProduct' is a Produit object

                    // Extract information from the 'product'
                    val title = selectedProduct.title
                    val price = selectedProduct.price
                    val quantity = selectedProduct.quantity
                    val image = selectedProduct.image

                    Log.d("CommandeAdapter", "Product Title: $title")
                    Log.d("CommandeAdapter", "Product Price: $price")
                    Log.d("CommandeAdapter", "Product Quantity: $quantity")
                    Log.d("CommandeAdapter", "Product Image: $image")
                }
            } else {
                // Handle the case when selectedProducts is empty
                // You may want to display a message or handle it based on your app logic
            }

            // Add other bindings as needed
        }
    }


}

