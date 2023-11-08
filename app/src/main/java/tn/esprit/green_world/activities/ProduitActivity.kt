package tn.esprit.green_world.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import tn.esprit.green_world.R
import tn.esprit.green_world.databinding.ActivityProduitBinding
import tn.esprit.green_world.fragments.ProduitFragment
import tn.esprit.green_world.models.Produit
import tn.esprit.green_world.viewModel.ProduitDetailViewModel

class ProduitActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProduitBinding
    private lateinit var produitId: String
    private lateinit var produitTitle: String
    private lateinit var produitDescription: String
    private lateinit var produitPrice: String
    private lateinit var produitQuantity: String
    private lateinit var produitImage: String
    private lateinit var produitMvvm: ProduitDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProduitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        produitMvvm = ViewModelProvider(this).get(ProduitDetailViewModel::class.java)

        getProduitInformationFromIntent()
        setInformationInViews()

        // Now that you have produitId, you can request data using the ViewModel
        produitMvvm.getProduitDetail(produitId)

        // Observe the LiveData after setting data and initiating the request
        observerProduitDetailLiveData()
    }

    private fun observerProduitDetailLiveData() {
        produitMvvm.obersverProduitDetailLiveData().observe(this, Observer { produit ->
            produit?.let {
                Log.d("ProduitActivity", "Product Data Received: ${produit.title}")
                binding.tvPrice.text = "Price: ${produit.price.toString()}TND"
                binding.tvDescription.text = "Description: ${produit.description}"

                if (produit.quantity > 0) {
                    binding.tvQuantity.text = "In Stock"
                    binding.tvQuantity.setTextColor(resources.getColor(R.color.green))
                } else {
                    binding.tvQuantity.text = "Out of Stock"
                    binding.tvQuantity.setTextColor(resources.getColor(R.color.red))
                }
            } ?: run {
                // Handle the case when the API response is null
                Toast.makeText(this, "Failed to retrieve product details", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getProduitInformationFromIntent() {
        val intent = intent
        produitId = intent.getStringExtra(ProduitFragment.Product_id) ?: ""
        produitTitle = intent.getStringExtra(ProduitFragment.Product_name) ?: ""
        produitDescription = intent.getStringExtra(ProduitFragment.Product_description) ?: ""
        produitPrice = intent.getStringExtra(ProduitFragment.Product_price) ?: ""
        produitQuantity = intent.getStringExtra(ProduitFragment.Product_quantity) ?: ""
        produitImage = intent.getStringExtra(ProduitFragment.Product_image) ?: ""

        Log.d("ProduitActivity", "Received produitId: $produitId")
        Log.d("ProduitActivity", "Received produitTitle: $produitTitle")
        Log.d("ProduitActivity", "Received produitDescription: $produitDescription")
        Log.d("ProduitActivity", "Received produitPrice: $produitPrice")
        Log.d("ProduitActivity", "Received produitQuantity: $produitQuantity")
        Log.d("ProduitActivity", "Received produitImage: $produitImage")
    }

    private fun setInformationInViews() {
        Glide.with(applicationContext)
            .load(produitImage)
            .into(binding.imgProduitDetail)
        binding.collapsingtoolbar.title = produitTitle
        binding.collapsingtoolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingtoolbar.setExpandedTitleColor(resources.getColor(R.color.darkgreen))
    }
}
