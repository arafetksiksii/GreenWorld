package tn.esprit.greenworld.ui.quiz_activity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.greenworld.R
import tn.esprit.greenworld.adapters.quiz.ClassementAdapter
import tn.esprit.greenworld.models.ClassementItem
import tn.esprit.greenworld.utils.QuizApi
import tn.esprit.greenworld.utils.RetrofitImp

class ClassementActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ClassementAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_classement) // Assurez-vous que c'est le bon layout

        recyclerView = findViewById(R.id.recycler_view_classement)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ClassementAdapter(emptyList())
        recyclerView.adapter = adapter
        // Appel API pour récupérer le classement
        fetchClassement()


        // Bouton pour fermer l'activité
        val btnClose: Button = findViewById(R.id.btn_close_classement)
        btnClose.setOnClickListener {
            finish() // Ferme l'activité
        }
    }


    private fun fetchClassement() {
        val quizApi = RetrofitImp.retrofit.create(QuizApi::class.java)
        quizApi.getClassement().enqueue(object : Callback<List<ClassementItem>> {
            override fun onResponse(call: Call<List<ClassementItem>>, response: Response<List<ClassementItem>>) {
                if (response.isSuccessful) {
                    val classementList = response.body() ?: emptyList()
                    // Mettre à jour l'adapter avec les données de classement
                    adapter = ClassementAdapter(classementList)
                    recyclerView.adapter = adapter
                } else {
                    Log.e("ClassementActivity", "Erreur: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<ClassementItem>>, t: Throwable) {
                Log.e("ClassementActivity", "Échec de la récupération des données: ${t.message}")
            }
        })
    }

}
