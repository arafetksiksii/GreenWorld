package com.example.pdm.activities

import Quiz
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pdm.R
import com.example.pdm.adapters.QuizAdapter
import com.example.pdm.interfaces.QuizApi
//import com.example.pdm.models.Quiz
import com.example.pdm.utils.RetrofitImp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
    }
}
