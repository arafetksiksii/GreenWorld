package tn.esprit.greenworld.ui.quiz_activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import tn.esprit.greenworld.R


class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result)

        val score = intent.getIntExtra("SCORE", 0)
        val totalQuestions = intent.getIntExtra("TOTAL_QUESTIONS", 0)
        val correctAnswers = intent.getIntExtra("CORRECT_ANSWERS", 0)


        val txtScore = findViewById<TextView>(R.id.txt_score)
        txtScore.text = "${(score.toDouble() / totalQuestions * 100).toInt()}% Score"
        val txtNbrQuestions = findViewById<TextView>(R.id.txt_nbr_questions)
        val txtNbrReponseCorrecte = findViewById<TextView>(R.id.txt_nbr_reponse_correcte)

        txtNbrQuestions.text = "$totalQuestions"
        txtNbrReponseCorrecte.text = "$correctAnswers"

        val backButton = findViewById<ImageView>(R.id.img_back)
        backButton.setOnClickListener {
            // Créer une intention pour démarrer une nouvelle activité
            val intent = Intent(this, QuizListActivity::class.java)
            // Démarrer l'activité
            startActivity(intent)
            // Optionnel : si vous ne voulez pas garder l'activité actuelle dans la pile d'activités
            finish()
        }

    }
}
