package tn.esprit.greenworld.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.greenworld.R
import tn.esprit.greenworld.databinding.ActivityProduitBinding
import tn.esprit.greenworld.fragments.ProduitFragment
import tn.esprit.greenworld.models.Commande
import tn.esprit.greenworld.models.Favproduit
import tn.esprit.greenworld.utils.RetrofitInstance
import tn.esprit.greenworld.viewmodel.ProduitDetailViewModel

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

        val commandeButton = binding.commandeButton
        val favproduitButton = binding.favProduitButton

        commandeButton.setOnClickListener {
            val produiId = produitId
            val call = RetrofitInstance.Commandeapi.addProductToCart(produitId)

            call.enqueue(object : Callback<Commande> {
                override fun onResponse(call: Call<Commande>, response: Response<Commande>) {
                    if (response.isSuccessful) {
                        // Handle the successful response here
                        val updatedCommande = response.body()
                        // You can do something with the updatedCommande if needed

                        // Display a Snackbar
                        val snackbar = Snackbar.make(
                            binding.root,
                            "Product added to cart",
                            Snackbar.LENGTH_LONG
                        )
                        snackbar.setAction("Visit Cart") {
                            // Handle click on "Visit Cart"
                            startActivity(Intent(applicationContext, CommandeActivity::class.java))
                        }
                        snackbar.show()
                    } else {
                        // Handle unsuccessful response
                        // You can parse the error response using response.errorBody()
                        Toast.makeText(applicationContext, "Failed to add product to cart", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Commande>, t: Throwable) {
                    // Handle failure
                    val snackbar = Snackbar.make(
                        binding.root,
                        "Product added to cart",
                        Snackbar.LENGTH_LONG
                    )
                    snackbar.setAction("Visit Cart") {
                        // Handle click on "Visit Cart"
                        startActivity(Intent(applicationContext, CommandeActivity::class.java))
                    }
                    snackbar.show()                }
            })
        }
        favproduitButton.setOnClickListener {

            val call = RetrofitInstance.Commandeapi.addProductToFav(produitId)

            call.enqueue(object : Callback<Favproduit> {
                override fun onResponse(call: Call<Favproduit>, response: Response<Favproduit>) {
                    if (response.isSuccessful) {
                        // Handle the successful response here
                        val updatedFavProduit = response.body()
                        // You can do something with the updatedFavProduit if needed

                        // Show a simple Toast message
                        Toast.makeText(applicationContext, "Product added to favorites", Toast.LENGTH_SHORT).show()

                        // Optionally, navigate to the Favorites activity

                        // Log success
                        Log.d("FavProduit", "Product added to favorites successfully")
                    } else {
                        // Handle unsuccessful response
                        // You can parse the error response using response.errorBody()
                        val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                        Log.e("FavProduit", "Failed to add product to favorites. Error: $errorMessage")

                        Toast.makeText(applicationContext, "Failed to add product to favorites", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Favproduit>, t: Throwable) {
                    // Handle failure
                    Log.e("FavProduit", "Failed to add product to favorites. Throwable: ${t.message}", t)

                    Toast.makeText(applicationContext, "Failed to add product to favorites", Toast.LENGTH_SHORT).show()
                }
            })
        }




    }

    private fun observerProduitDetailLiveData() {
        produitMvvm.obersverProduitDetailLiveData().observe(this, Observer { produit ->
            produit?.let {
                Log.d("ProduitActivity", "Product Data Received: ${produit._id}")
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
        Log.d("ProduitImage", produitImage)
        binding.collapsingtoolbar.title = produitTitle
        binding.collapsingtoolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingtoolbar.setExpandedTitleColor(resources.getColor(R.color.darkgreen))
    }
}