package tn.esprit.greenworld

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import tn.esprit.greenworld.activities.Constants.PREF_NAME
import tn.esprit.greenworld.activities.Constants.USER_ID_KEY
import tn.esprit.greenworld.adapters.dechetsAdapter
import tn.esprit.greenworld.databinding.FragmentHomedechetsBinding
import tn.esprit.greenworld.fragments.typedechets
import tn.esprit.greenworld.viewmodel.dechetviewmodel


class homedechets : Fragment() {
    private lateinit var binding: FragmentHomedechetsBinding
    private lateinit var dechetsMvvm: dechetviewmodel
    private lateinit var DechetsListAdapter: dechetsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dechetsMvvm = ViewModelProvider(this).get(dechetviewmodel::class.java)
        DechetsListAdapter = dechetsAdapter()


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomedechetsBinding.inflate(inflater, container, false)

        val wastesReportsCard = binding.root.findViewById<CardView>(R.id.wastesReportsCard)
        val reportHistoryCard = binding.root.findViewById<CardView>(R.id.reportHistoryCard)
        val rewardPointsCard = binding.root.findViewById<CardView>(R.id.rewardPointsCard)


        // Ajouter des listeners de clics
        wastesReportsCard.setOnClickListener {
            navigateToTypedechetsFragment()
        }

        reportHistoryCard.setOnClickListener {
            navigateToHistoryDechetsActivity()
        }

        rewardPointsCard.setOnClickListener {
            navigateToPointDechetsActivity()
        }
        return binding.root


    }

    private fun navigateToTypedechetsFragment() {
        // Naviguer vers le fragment typedechets
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, typedechets())
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun navigateToHistoryDechetsActivity() {
        // Naviguer vers l'activité HistoryDechets
        val intent = Intent(activity, HistoryDechets::class.java)
        startActivity(intent)
    }

    private fun navigateToPointDechetsActivity() {
        // Naviguer vers l'activité pointdechets
        val intent = Intent(activity, pointdechets::class.java)
        startActivity(intent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        prepareListDechetstRecyclerView()
        // val userId = "655ddc80d5849d7349c150f6"
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString(USER_ID_KEY, "")
        if (!userId.isNullOrEmpty()) {
            dechetsMvvm.getDechets(userId)
            observeListdechetsLiveData()
            onListdechetClicked()
        } else {
            Log.e("homedechets", "User ID not found in SharedPreferences")
        }



    }
    private fun prepareListDechetstRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            adapter = DechetsListAdapter
        }
        Log.d("dechets", "RecyclerView prepared")
    }

    private fun observeListdechetsLiveData() {
        dechetsMvvm.observeListDechetLiveData().observe(viewLifecycleOwner, Observer { dechets ->
            dechets?.let {
                Log.d("dechets", "Received ${dechets.size} dechets")
                DechetsListAdapter.setdechetList(dechets)
                Log.d("dechets", "Adapter updated")
            } ?: run {
                Log.d("dechets", "List API response body is null")
            }
        })
    }

    private fun onListdechetClicked() {
        DechetsListAdapter.onListDechetsClicked = { dechets ->
            Log.d("dechets", " dechets Clicked: ${dechets.Type_dechets}")
            Log.d("dechets", "dechets ID: ${dechets._id}")
            Log.d("dechets", "dechets Title: ${dechets.adresse}")
            Log.d("dechets", "dechets Date: ${dechets.date_depot}")
            Log.d("dechets", "dechets Location: ${dechets.nombre_capacite}")




        }
    }





}


