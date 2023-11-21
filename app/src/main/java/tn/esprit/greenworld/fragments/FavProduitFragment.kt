package tn.esprit.greenworld.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tn.esprit.greenworld.adapters.FavProduitAdapter
import tn.esprit.greenworld.databinding.FavproduitFragmentBinding
import tn.esprit.greenworld.utils.RetrofitInstance
import tn.esprit.greenworld.viewmodel.FavProduitViewModel

class FavProduitFragment : Fragment() {

    private lateinit var favProduitViewModel: FavProduitViewModel
    private lateinit var favProduitAdapter: FavProduitAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FavproduitFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FavproduitFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView
        recyclerView = binding.recyclerView2
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        favProduitAdapter = FavProduitAdapter(RetrofitInstance)
        recyclerView.adapter = favProduitAdapter

        // Set up ViewModel
        favProduitViewModel = ViewModelProvider(this).get(FavProduitViewModel::class.java)

        // Observe the LiveData from ViewModel
        favProduitViewModel.getFavproduit().observe(viewLifecycleOwner, { favProduit ->
            favProduit?.let {
                favProduitAdapter.setFavProduit(it)
            }
        })
    }
}
