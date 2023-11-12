package tn.esprit.green_world.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tn.esprit.green_world.R
import tn.esprit.green_world.adapters.CommandeAdapter
import tn.esprit.green_world.viewModel.CommandeViewModel

class CommandeActivity : AppCompatActivity() {

    private lateinit var commandeViewModel: CommandeViewModel
    private lateinit var commandeAdapter: CommandeAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_commande)

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        commandeAdapter = CommandeAdapter()
        recyclerView.adapter = commandeAdapter

        // Set up ViewModel
        commandeViewModel = ViewModelProvider(this).get(CommandeViewModel::class.java)

        // Observe the LiveData from ViewModel
        commandeViewModel.getCommande().observe(this, { commande ->
            Log.d("CommandeActivity", "Number of products in Commande: ${commande?.selectedProducts!!.size}")

            commande?.let {
                commandeAdapter.setCommande(it)
                Log.d("CommandeActivity", "Received Commande data from ViewModel.")
            }
        })
    }

}
