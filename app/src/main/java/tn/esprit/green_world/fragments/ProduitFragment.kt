package tn.esprit.green_world.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import tn.esprit.green_world.databinding.FragmentProduitBinding
import tn.esprit.green_world.viewModel.ProduitViewModel
import com.bumptech.glide.Glide
import tn.esprit.green_world.activities.ProduitActivity
import tn.esprit.green_world.models.Produit

class ProduitFragment : Fragment() {
    private lateinit var binding: FragmentProduitBinding
    private lateinit var produitMvvm: ProduitViewModel
    private lateinit var randomProduit:Produit
    companion object{
        const val Product_id="tn.esprit.green_world.fragments.idProduit"
        const val Product_name="tn.esprit.green_world.fragments.nomProduit"
        const val Product_description="tn.esprit.green_world.fragments.descriptionProduit"
        const val Product_price="tn.esprit.green_world.fragments.descriptionProduit"
        const val Product_image="tn.esprit.green_world.fragments.imageProduit"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        produitMvvm = ViewModelProvider(this).get(ProduitViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProduitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        produitMvvm.getRandomProduit()
        observerRandomProduit()
        onRandomProductClick()

    }

    private fun onRandomProductClick() {
        binding.randomProduitCard.setOnClickListener{
            val intent = Intent(activity,ProduitActivity::class.java)
            intent.putExtra(Product_id,randomProduit._id)
            intent.putExtra(Product_name,randomProduit.title)
            intent.putExtra(Product_description,randomProduit.description)
            intent.putExtra(Product_description,randomProduit.price)
            intent.putExtra(Product_image,randomProduit.image)
            startActivity(intent)
        }
    }

    private fun observerRandomProduit() {
        produitMvvm.observeRandomProduitLiveData().observe(viewLifecycleOwner,object :Observer<Produit>{
            override fun onChanged(t: Produit) {
                randomProduit = t!!
                Glide.with(this@ProduitFragment)
                    .load(t!!.image)
                    .into(binding.imgRandomProduit)

            }


        })


    }
}
