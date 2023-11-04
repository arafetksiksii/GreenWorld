package tn.esprit.greenworld.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import tn.esprit.greenworld.databinding.FragmentMagasinBinding
import tn.esprit.greenworld.databinding.SingleItemProduitBinding
import tn.esprit.greenworld.models.Produit
import tn.esprit.greenworld.utils.AppDatabase

class ProduitAdapter(val produitlist: MutableList<Produit>) : RecyclerView.Adapter<ProduitAdapter.ProduitHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProduitHolder {
        val binding = SingleItemProduitBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProduitHolder(binding)
    }

    override fun onBindViewHolder(holder: ProduitHolder, position: Int) {
        with(holder) {
            with(produitlist[position]) {
                binding.tfTitle.text = title
                binding.tfPrice.text = price



            }
        }
    }

    override fun getItemCount() =produitlist.size

    inner class ProduitHolder(val binding: SingleItemProduitBinding) : RecyclerView.ViewHolder(binding.root)
}