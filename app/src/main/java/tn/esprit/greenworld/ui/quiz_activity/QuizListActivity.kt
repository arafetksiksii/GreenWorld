package tn.esprit.greenworld.ui.quiz_activity

import android.content.Intent
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
import tn.esprit.greenworld.adapters.quiz.QuizAdapter
import tn.esprit.greenworld.models.Quiz
import tn.esprit.greenworld.utils.QuizApi
import tn.esprit.greenworld.utils.RetrofitImp

class QuizListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: QuizAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_quiz)

        recyclerView = findViewById(R.id.RV_list_quiz)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = QuizAdapter(this, emptyList())
        recyclerView.adapter = adapter

        val quizApi = RetrofitImp.retrofit.create(QuizApi::class.java)
        quizApi.getQuizzes().enqueue(object : Callback<List<Quiz>> {
            override fun onResponse(call: Call<List<Quiz>>, response: Response<List<Quiz>>) {
                if (response.isSuccessful) {
                    val quizzes = response.body()
                    if (quizzes != null) {
                        adapter.setQuizList(quizzes)
                    }
                } else {
                    Log.e("QuizListActivity", "Error: ${response.code()} ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Quiz>>, t: Throwable) {
                Log.e("QuizListActivity", "Failed to fetch quizzes: ${t.message}")
            }
        })

        val btnVoirClassement: Button = findViewById(R.id.btn_voir_classement)
        btnVoirClassement.setOnClickListener {
            // Démarrer l'activité ClassementActivity
            val intent = Intent(this, ClassementActivity::class.java)
            startActivity(intent)
        }
    }
}
