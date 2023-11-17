package tn.esprit.greenworld.ui.quiz_activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import tn.esprit.greenworld.R
import tn.esprit.greenworld.models.Question
import tn.esprit.greenworld.utils.QuizApi
import tn.esprit.greenworld.utils.RetrofitImp

class MainActivity : AppCompatActivity() {

    private var questions: List<Question> = listOf()
    private var currentQuestionIndex = 0
    private val userResponses = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val txtQuiz = findViewById<TextView>(R.id.txt_quiz)
        val txtQuestionNum = findViewById<TextView>(R.id.txt_question_num)
        val txtQuestion = findViewById<TextView>(R.id.txt_question)
        val btnChoix1 = findViewById<Button>(R.id.btn_choix1)
        val btnChoix2 = findViewById<Button>(R.id.btn_choix2)
        val btnChoix3 = findViewById<Button>(R.id.btn_choix3)
        val btnChoix4 = findViewById<Button>(R.id.btn_choix4)
        val btnNextQuestion = findViewById<Button>(R.id.btn_voir_resultat)
        val quizId = intent.getStringExtra("quizId") ?: return
        val quizApi = RetrofitImp.retrofit.create(QuizApi::class.java)

        quizApi.getQuestionsForQuiz(quizId).enqueue(object : Callback<List<Question>> {
            override fun onResponse(call: Call<List<Question>>, response: retrofit2.Response<List<Question>>) {
                if (response.isSuccessful) {
                    questions = response.body() ?: emptyList()
                    // Afficher la première question
                    showQuestion(questions?.firstOrNull())
                }
            }

            override fun onFailure(call: Call<List<Question>>, t: Throwable) {
                // Gérer l'échec ici, par exemple en affichant un message d'erreur
            }
        })



        // Configuration des écouteurs de clics pour les boutons de choix
        btnChoix1.setOnClickListener {
            showNextQuestion(0)
        }
        btnChoix2.setOnClickListener {
            showNextQuestion(1)
        }
        btnChoix3.setOnClickListener {
            showNextQuestion(2)
        }
        btnChoix4.setOnClickListener {
            showNextQuestion(3)
        }
        btnNextQuestion.setOnClickListener {
            // Gérez le clic pour passer à la question suivante
        }
    }

    private fun showQuestion(question: Question?) {
        // Assurez-vous que la question n'est pas nulle
        question?.let {
            // Mettez à jour le texte de la question
            val txtQuestion = findViewById<TextView>(R.id.txt_question)
            txtQuestion.text = it.question

            val btnVoirResultat = findViewById<Button>(R.id.btn_voir_resultat)
            if (currentQuestionIndex == questions.size - 1) {
                // C'est la dernière question
                btnVoirResultat.visibility = View.VISIBLE
                btnVoirResultat.setOnClickListener {
                    calculateScoreAndShowResult()
                }

            } else {
                // Ce n'est pas la dernière question
                btnVoirResultat.visibility = View.GONE
            }

            // Mettez à jour les boutons de choix avec les options de la question
            val btnChoix1 = findViewById<Button>(R.id.btn_choix1)
            val btnChoix2 = findViewById<Button>(R.id.btn_choix2)
            val btnChoix3 = findViewById<Button>(R.id.btn_choix3)
            val btnChoix4 = findViewById<Button>(R.id.btn_choix4)

            // Supposons que 'choix' est une liste de String dans votre modèle Question
            btnChoix1.text = it.choix.getOrElse(0) { "" }
            btnChoix2.text = it.choix.getOrElse(1) { "" }
            btnChoix3.text = it.choix.getOrElse(2) { "" }
            btnChoix4.text = it.choix.getOrElse(3) { "" }

            // Configurez les écouteurs de clic pour chaque bouton si nécessaire
            btnChoix1.setOnClickListener { showNextQuestion(0) }
            btnChoix2.setOnClickListener { showNextQuestion(1) }
            btnChoix3.setOnClickListener { showNextQuestion(2) }
            btnChoix4.setOnClickListener { showNextQuestion(3) }
        } ?: run {
            // Si la question est nulle, vous pouvez afficher un message d'erreur ou fermer l'activité
        }
    }

    private fun showNextQuestion(userResponse: Int) {
        // Enregistrer la réponse de l'utilisateur
        userResponses.add(userResponse)

        // Passer à la question suivante
        currentQuestionIndex++
        if (currentQuestionIndex < questions.size) {
            showQuestion(questions[currentQuestionIndex])
        } else {
            // Toutes les questions ont été répondues
            // Vous pouvez maintenant calculer le score ou passer à un nouvel écran
            calculateScore()
        }
    }

    private fun calculateScore(): Int {
        var score = 0
        // Supposons que 'questions' est une liste de vos objets Question et que 'userResponses' est une liste des indices des réponses de l'utilisateur
        questions.forEachIndexed { index, question ->
            // Vérifiez si l'indice de la réponse de l'utilisateur correspond à l'indice de la réponse correcte de la question
            if (question.reponse_correcte == userResponses[index]) {
                score++
            }
        }
        return score
    }


    private fun calculateScoreAndShowResult() {
        var correctAnswers = 0
        val score = calculateScore()
        questions.forEachIndexed { index, question ->
            // Ici on suppose que la reponse_correcte dans le modèle Question est l'indice de la bonne réponse
            if (question.reponse_correcte == userResponses[index]) {
                correctAnswers++
            }
        }
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("SCORE", score)
        intent.putExtra("TOTAL_QUESTIONS", questions.size)
        intent.putExtra("CORRECT_ANSWERS", correctAnswers)

        startActivity(intent)
        finish() // Optionnel, termine l'activité actuelle si vous ne voulez pas que l'utilisateur revienne au quiz
    }



}

