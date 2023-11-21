package tn.esprit.greenworld.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tn.esprit.greenworld.viewModel.CommandeViewModel
import tn.esprit.greenworld.R
import tn.esprit.greenworld.adapters.CommandeAdapter
import tn.esprit.greenworld.databinding.ActivityCommandeBinding
import tn.esprit.greenworld.utils.RetrofitInstance

class CommandeActivity : AppCompatActivity() {

    private lateinit var commandeViewModel: CommandeViewModel
    private lateinit var commandeAdapter: CommandeAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: ActivityCommandeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_commande)

        val btnProceedToPayment: Button = findViewById(R.id.btnProceedToPayment)
        btnProceedToPayment.setOnClickListener {
            // Navigate to the payment activity
            val intent = Intent(this, PaymentActivity::class.java)
            startActivity(intent)
        }
        // Set up RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        commandeAdapter = CommandeAdapter(RetrofitInstance)
        recyclerView.adapter = commandeAdapter

        // Set up ViewModel
        commandeViewModel = ViewModelProvider(this).get(CommandeViewModel::class.java)

        // Observe the LiveData from ViewModel
        commandeViewModel.getCommande().observe(this, { commande ->
            Log.d("CommandeActivity", "Number of products in Commande: ${commande?.selectedProducts!!.size}")

            commande?.let {
                commandeAdapter.setCommande(it)
                commandeAdapter.setTotalPrice(it.totalPrice) // Assuming totalPrice is a Double or any appropriate type

                Log.d("CommandeActivity", "Received Commande data from ViewModel.")
            }
        })

    }

}
