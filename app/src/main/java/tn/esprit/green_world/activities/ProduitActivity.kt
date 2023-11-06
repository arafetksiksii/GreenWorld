package tn.esprit.green_world.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import tn.esprit.green_world.R
import tn.esprit.green_world.databinding.ActivityProduitBinding
import tn.esprit.green_world.databinding.FragmentProduitBinding
import tn.esprit.green_world.fragments.ProduitFragment

class ProduitActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProduitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProduitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve data from the intent
        val produitId = intent.getStringExtra(ProduitFragment.Product_id)
        val produitTitle = intent.getStringExtra(ProduitFragment.Product_name)
        val produitDescription = intent.getStringExtra(ProduitFragment.Product_description)
        val produitImage = intent.getStringExtra(ProduitFragment.Product_image)
        val produitPrice = intent.getStringExtra(ProduitFragment.Product_price)

        if (produitId != null && produitTitle != null && produitImage != null) {
            // Data exists, set it in the views
            setInformationInViews(produitTitle, produitImage)
        } else {
            // Handle the case when data is missing or invalid
            // You can show an error message, log the issue, or perform any appropriate action
        }
    }

    private fun setInformationInViews(produitTitle: String, produitImage: String) {
        Glide.with(applicationContext)
            .load(produitImage)
            .into(binding.imgProduitDetail)
        binding.collapsingtoolbar.title = produitTitle
        binding.collapsingtoolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingtoolbar.setExpandedTitleColor(resources.getColor(R.color.darkgreen))
    }
}
