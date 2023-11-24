package tn.esprit.greenworld.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import tn.esprit.greenworld.databinding.FragmentProduitBinding
import tn.esprit.greenworld.activities.ProduitActivity
import tn.esprit.greenworld.adapters.MostPopularProduct
import tn.esprit.greenworld.models.Produit
import tn.esprit.greenworld.viewmodel.ProduitViewModel
import com.bumptech.glide.Glide
import tn.esprit.greenworld.adapters.ProduitListAdapter

class ProduitFragment : Fragment() {
    private lateinit var binding: FragmentProduitBinding
    private lateinit var produitMvvm: ProduitViewModel
    private lateinit var randomProduit: Produit
    private lateinit var popularProduitAdapter: MostPopularProduct
    private lateinit var produitListAdapter: ProduitListAdapter

    companion object {
        const val Product_id = "tn.esprit.green_world.fragments.idProduit"
        const val Product_name = "tn.esprit.green_world.fragments.nomProduit"
        const val Product_description = "tn.esprit.green_world.fragments.descriptionProduit"
        const val Product_price = "tn.esprit.green_world.fragments.priceProduit"
        const val Product_quantity = "tn.esprit.green_world.fragments.quantityProduit"
        const val Product_image = "tn.esprit.green_world.fragments.imageProduit"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        produitMvvm = ViewModelProvider(this).get(ProduitViewModel::class.java)
        popularProduitAdapter = MostPopularProduct()
        produitListAdapter = ProduitListAdapter()
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

        preparePopularProduitRecyclerView()

        produitMvvm.getPopularProduit()
        OnPopularProductClicked()

        observerRandomProduit()
        onRandomProductClick()

        produitMvvm.getRandomProduit()
        observerPopularProduitLiveData()

        // Fetch popular products from the ViewModel

        prepareListProduitRecyclerView()
        produitMvvm.getProducts()
        observeListProduitLiveData()
        onListProductClicked()

        prepareListProduitRecyclerView()
    }

    private fun onListProductClicked() {
        produitListAdapter.onListProductClick = { produit ->
            Log.d("ProduitFragment", " Product Clicked: ${produit.description}")
            Log.d("ProduitFragment", "Product ID: ${produit._id}")
            Log.d("ProduitFragment", "Product Title: ${produit.title}")

            val intent = Intent(activity, ProduitActivity::class.java)
            intent.putExtra(ProduitFragment.Product_id, produit._id)

            intent.putExtra(ProduitFragment.Product_name, produit.title)
            intent.putExtra(ProduitFragment.Product_description, produit.description)
            intent.putExtra(ProduitFragment.Product_image, produit.image)
            intent.putExtra(ProduitFragment.Product_price, produit.price.toString()) // Convert price to string
            intent.putExtra(ProduitFragment.Product_quantity, produit.quantity.toString()) // Convert quantity to string
            startActivity(intent)
        }
    }

    private fun prepareListProduitRecyclerView() {
        binding.recViewStore.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = produitListAdapter
        }
        Log.d("ProduitFragment", "RecyclerView prepared")
    }


    private fun observeListProduitLiveData() {
        produitMvvm.observeListProduitLiveData().observe(viewLifecycleOwner, Observer { produits ->
            produits?.let {
                Log.d("ProduitFragment", "Received ${produits.size} products")
                produitListAdapter.setProduitList(produits)
                Log.d("ProduitFragment", "Adapter updated")
            } ?: run {
                Log.d("ProduitFragment", "List API response body is null")
            }
        })
    }



    private fun OnPopularProductClicked() {
        popularProduitAdapter.onMostpopularProductClick = { produit ->
            Log.d("ProduitFragment", "Popular Product Clicked: ${produit.description}")
            Log.d("ProduitFragment", "Product ID: ${produit._id}")
            Log.d("ProduitFragment", "Product Title: ${produit.title}")

            val intent = Intent(activity, ProduitActivity::class.java)
            intent.putExtra(ProduitFragment.Product_id, produit._id)
            intent.putExtra(ProduitFragment.Product_name, produit.title)
            intent.putExtra(ProduitFragment.Product_description, produit.description)
            intent.putExtra(ProduitFragment.Product_image, produit.image)
            intent.putExtra(ProduitFragment.Product_price, produit.price.toString()) // Convert price to string
            intent.putExtra(ProduitFragment.Product_quantity, produit.quantity.toString()) // Convert quantity to string
            startActivity(intent)
        }
    }

    private fun preparePopularProduitRecyclerView() {
        binding.recViewProduitsPopular.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularProduitAdapter
        }
    }

    private fun observerPopularProduitLiveData() {
        produitMvvm.observePopularProduitLiveData().observe(viewLifecycleOwner, Observer { produitList ->
            produitList?.let {
                popularProduitAdapter.setProduit(produitList)
            } ?: run {
                Log.d("ProduitFragment", "Popular API response body is null")
            }
        })
    }

    private fun onRandomProductClick() {
        binding.randomProduitCard.setOnClickListener {
            val intent = Intent(activity, ProduitActivity::class.java)
            intent.putExtra(Product_id, randomProduit._id)
            intent.putExtra(Product_name, randomProduit.title)
            intent.putExtra(Product_description, randomProduit.description)
            intent.putExtra(Product_price, randomProduit.price.toString()) // Convert price to string
            intent.putExtra(Product_image, randomProduit.image)
            intent.putExtra(Product_quantity, randomProduit.quantity.toString()) // Convert quantity to string
            startActivity(intent)
        }
    }

    private fun observerRandomProduit() {
        produitMvvm.observeRandomProduitLiveData().observe(viewLifecycleOwner, Observer { t ->
            t?.let {
                randomProduit = t
                Glide.with(this)
                    .load(t.image)
                    .into(binding.imgRandomProduit)
            }
        })
    }
}