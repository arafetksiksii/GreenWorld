package tn.esprit.greenworld.ui.quiz_activity

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.greenworld.R
import tn.esprit.greenworld.models.Question
import tn.esprit.greenworld.utils.QuizApi
import tn.esprit.greenworld.utils.RetrofitImp
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private var questions: List<Question> = listOf()
    private var currentQuestionIndex = 0
    private val userResponses = mutableListOf<Int>()
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var txtCompteur: TextView
    private val timeLeftInMillis: Long = 30000 // 30 secondes pour chaque question
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtCompteur = findViewById(R.id.txt_compteur)
        val btnChoix1 = findViewById<Button>(R.id.btn_choix1)
        val btnChoix2 = findViewById<Button>(R.id.btn_choix2)
        val btnChoix3 = findViewById<Button>(R.id.btn_choix3)
        val btnChoix4 = findViewById<Button>(R.id.btn_choix4)
        val btnVoirResultat = findViewById<Button>(R.id.btn_voir_resultat)
        val quizId = intent.getStringExtra("quizId") ?: return
        val quizApi = RetrofitImp.retrofit.create(QuizApi::class.java)

        quizApi.getQuestionsForQuiz(quizId).enqueue(object : Callback<List<Question>> {
            override fun onResponse(call: Call<List<Question>>, response: Response<List<Question>>) {
                if (response.isSuccessful) {
                    questions = response.body() ?: emptyList()
                    showQuestion(questions.firstOrNull())
                }
            }

            override fun onFailure(call: Call<List<Question>>, t: Throwable) {
                // Gérer l'échec ici
            }
        })

        val choiceButtons = listOf(btnChoix1, btnChoix2, btnChoix3, btnChoix4)
        choiceButtons.forEachIndexed { index, button ->
            button.setOnClickListener { recordAnswerAndShowNextQuestion(index) }
        }

        btnVoirResultat.setOnClickListener {
            calculateScoreAndShowResult()        }

        // Initialisation du MediaPlayer pour l'effet sonore
        mediaPlayer = MediaPlayer.create(this, R.raw.matrrioox_ticking_timer_05_sec)
    }

    private fun showQuestion(question: Question?) {
        question?.let {
            val txtQuestion = findViewById<TextView>(R.id.txt_question)
            txtQuestion.text = it.question

            val buttons = listOf(
                findViewById<Button>(R.id.btn_choix1),
                findViewById<Button>(R.id.btn_choix2),
                findViewById<Button>(R.id.btn_choix3),
                findViewById<Button>(R.id.btn_choix4)
            )

            buttons.forEachIndexed { index, button ->
                button.text = it.choix.getOrElse(index) { "" }
                button.visibility = if (it.choix.size > index) View.VISIBLE else View.GONE
            }

            val btnVoirResultat = findViewById<Button>(R.id.btn_voir_resultat)
            btnVoirResultat.visibility = if (currentQuestionIndex >= questions.size - 1) View.VISIBLE else View.GONE

            startCountDown()
        }
    }

    private fun startCountDown() {
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                txtCompteur.text = String.format(Locale.getDefault(), "%02d", secondsLeft)

                // Jouer un son durant les 5 dernières secondes
                if (secondsLeft <= 5) {
                    mediaPlayer.start()
                }
            }

            override fun onFinish() {
                txtCompteur.text = getString(R.string.time_up)
                mediaPlayer.stop() // Arrêtez le son lorsqu'il est terminé
                recordAnswerAndShowNextQuestion(-1) // -1 pour une réponse non donnée
            }
        }.start()
    }

    private fun recordAnswerAndShowNextQuestion(userResponse: Int) {
        userResponses.add(userResponse)
        countDownTimer.cancel()

        if (currentQuestionIndex < questions.size - 1) {
            currentQuestionIndex++
            showQuestion(questions[currentQuestionIndex])
        } else {
            // Ne démarrez pas l'activité ResultActivity ici
            // Juste afficher le bouton pour voir le résultat
            val btnVoirResultat = findViewById<Button>(R.id.btn_voir_resultat)
            btnVoirResultat.visibility = View.VISIBLE
        }
    }

    private fun calculateScoreAndShowResult() {
        var score = 0
        questions.forEachIndexed { index, question ->
            if (question.reponse_correcte == userResponses[index]) {
                score++
            }
        }

        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("SCORE", score)
        intent.putExtra("TOTAL_QUESTIONS", questions.size)
        intent.putExtra("CORRECT_ANSWERS", score)
        startActivity(intent)
        finish()
    }

    override fun onPause() {
        super.onPause()
        if (this::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.release()
    }
}
