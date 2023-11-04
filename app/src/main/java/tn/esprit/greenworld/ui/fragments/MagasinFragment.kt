package tn.esprit.greenworld.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import tn.esprit.greenworld.R
import tn.esprit.greenworld.adapters.ProduitAdapter
import tn.esprit.greenworld.databinding.FragmentMagasinBinding
import tn.esprit.greenworld.models.Produit


class ProduitFragment : Fragment() {
    private lateinit var binding: FragmentMagasinBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMagasinBinding.inflate(layoutInflater)

        binding.rvProduit.adapter = ProduitAdapter(getListProduit(requireContext()))
        binding.rvProduit.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        return binding.root
    }

    private fun getListProduit(context: Context) : MutableList<Produit> {
        return mutableListOf(
            Produit(1, R.drawable.ic_camera, context.getString(R.string.produitD1), context.getString(R.string.produitPrice1) , context.getString(R.string.produitPrice1),context.getString(R.string.produitQuantity1)),
            Produit(2, R.drawable.ic_home, context.getString(R.string.produitD2), context.getString(R.string.produitPrice1) , context.getString(R.string.produitPrice2),context.getString(R.string.produitQuantity2)),
            Produit(3, R.drawable.ic_heart, context.getString(R.string.produitD3), context.getString(R.string.produitPrice1) , context.getString(R.string.produitPrice3),context.getString(R.string.produitQuantity3)),

        )
    }

}