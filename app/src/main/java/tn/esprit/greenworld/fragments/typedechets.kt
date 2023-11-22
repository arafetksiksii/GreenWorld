package tn.esprit.greenworld.fragments

import android.app.Activity.RESULT_OK
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
import tn.esprit.greenworld.activities.Maindechets
import tn.esprit.greenworld.adapters.typeAdapter


import tn.esprit.greenworld.databinding.FragmentTypedechetsBinding
import tn.esprit.greenworld.viewmodel.TypeViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [typedechets.newInstance] factory method to
 * create an instance of this fragment.
 */
class typedechets : Fragment() {
    // TODO: Rename and change types of parameters

    private lateinit var binding: FragmentTypedechetsBinding
    private lateinit var typeMvvm: TypeViewModel
    private lateinit var TypeListAdapter: typeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        typeMvvm = ViewModelProvider(this).get(TypeViewModel::class.java)
        TypeListAdapter = typeAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTypedechetsBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {

        const val type_id = "com.example.testtt.fragments.idtype"
        const val type_titre = "com.example.testtt.fragments.titre"
        const val type_image= "com.example.testtt.fragments.image_type"
        const val REQUEST_CODE_MAINDECHETS = 1001

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        prepareListTypeRecyclerView()
        typeMvvm.getType()
        observeListTypeLiveData()
        onListTypeClicked()

        prepareListTypeRecyclerView()
    }
    private fun onListTypeClicked() {
        TypeListAdapter.onListTypeClicked = { type ->


            val intent = Intent(activity, Maindechets::class.java)
            intent.putExtra(typedechets.type_id, type._id)
            intent.putExtra(typedechets.type_titre, type.titre)
            intent.putExtra(typedechets.type_image, type.image_type)
            startActivityForResult(intent, REQUEST_CODE_MAINDECHETS)

        }
    }

    private fun prepareListTypeRecyclerView() {
        binding.recViewStore.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            adapter = TypeListAdapter
        }
        Log.d("TypeFragment", "RecyclerView prepared")
    }
    private fun observeListTypeLiveData() {
        // Dans votre fragment
        typeMvvm.observeListTypeLiveData().observe(viewLifecycleOwner, Observer { type ->
            type?.let {
                Log.d("TypeFragment", "Received ${type.size} types")
                TypeListAdapter.setTypeList(type)
                Log.d("TypeFragment", "Adapter updated")
            } ?: run {
                Log.d("TypeFragment", "List API response body is null")
            }
        })

    }


}