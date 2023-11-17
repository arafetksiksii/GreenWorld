package com.example.pdm.adapters

import Quiz
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pdm.MainActivity
import com.example.pdm.R

//import com.example.pdm.models.Quiz

class QuizAdapter(private val context: Context, private var QuizList: List<Quiz>) : RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {

    fun setQuizList(userList: List<Quiz>) {
        this.QuizList = userList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_quiz, parent, false)
        return QuizViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val quiz = QuizList[position]
        holder.bind(quiz)
        holder.itemView.findViewById<Button>(R.id.btn_participer).setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            // Vous pouvez également passer des données supplémentaires si nécessaire, par exemple:
            intent.putExtra("quizId", quiz._id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return QuizList.size
    }

    class QuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val quizTitleTextView: TextView = itemView.findViewById(R.id.txt_quiz_title)
        private val quizDescTextView: TextView = itemView.findViewById(R.id.txt_quiz_desc)

        fun bind(quiz: Quiz) {
            quizTitleTextView.text = quiz.nom_quiz
            quizDescTextView.text = quiz.description_quiz
            // Here you can bind more data to other views if you have them
        }
    }
}

