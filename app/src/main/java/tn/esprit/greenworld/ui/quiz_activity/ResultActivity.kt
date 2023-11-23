package tn.esprit.greenworld.ui.quiz_activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okio.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.greenworld.R
import tn.esprit.greenworld.models.QuestionAndAnswers
import tn.esprit.greenworld.models.User
import tn.esprit.greenworld.models.User4
import tn.esprit.greenworld.ui.gestionUser.LoginActivity
import tn.esprit.greenworld.utils.RetrofitImp
import tn.esprit.greenworld.utils.UserInterface
import java.io.File
import java.io.FileOutputStream

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result)

        val gson = Gson()
        val questionAndAnswersJson = intent.getStringExtra("QUESTIONS_AND_ANSWERS")
        val typee = object : TypeToken<List<QuestionAndAnswers>>() {}.type
        val questionAndAnswersList = gson.fromJson<List<QuestionAndAnswers>>(questionAndAnswersJson, typee)


        // Initialisation du MediaPlayer pour l'effet sonore des résultats
        val mediaPlayer = MediaPlayer.create(this, R.raw.result_congratulations)
        mediaPlayer.start()

        mediaPlayer.setOnCompletionListener { mp -> mp.release() }

        val score = intent.getIntExtra("SCORE", 0)
        val totalQuestions = intent.getIntExtra("TOTAL_QUESTIONS", 0)

        val txtScore = findViewById<TextView>(R.id.txt_score)
        txtScore.text = "${(score.toDouble() / totalQuestions * 100).toInt()}% Score"

        val txtNbrQuestions = findViewById<TextView>(R.id.txt_nbr_questions)
        txtNbrQuestions.text = "$totalQuestions"

        val txtNbrReponseCorrecte = findViewById<TextView>(R.id.txt_nbr_reponse_correcte)
        txtNbrReponseCorrecte.text = "$score"

        val sharedPreferences: SharedPreferences = getSharedPreferences("user_pref", MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", "")

        userId?.let { updateScore(it, score) }

        val backButton = findViewById<ImageView>(R.id.img_back)
        backButton.setOnClickListener {
            val intent = Intent(this, QuizListActivity::class.java)
            startActivity(intent)
            finish()
        }

        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(700, 1200, 1).create()
        val page = pdfDocument.startPage(pageInfo)


        val canvas = page.canvas
        val paint = Paint().apply { textSize = 30f }

        // Positionnez l'image en haut du document
        var y = 25f  // Un peu d'espace à partir du haut



        canvas.drawText("Résultats du Quiz", 10f, y, paint)
        y += paint.descent() - paint.ascent()

        canvas.drawText("ID de l'utilisateur: $userId", 10f, y, paint)
        y += paint.descent() - paint.ascent()

        canvas.drawText("Score: $score / $totalQuestions", 10f, y, paint)
        y += paint.descent() - paint.ascent()
        // Ajouter les questions et les réponses dans le PDF
        for (qa in questionAndAnswersList) {
            // Écrire la question
            canvas.drawText("Question: ${qa.question}", 10f, y, paint)
            y += paint.descent() - paint.ascent()

            // Écrire la réponse de l'utilisateur
            canvas.drawText("Votre réponse: ${qa.userAnswer}", 10f, y, paint)
            y += paint.descent() - paint.ascent()

            // Écrire la bonne réponse
            canvas.drawText("Réponse correcte: ${qa.correctAnswer}", 10f, y, paint)
            y += paint.descent() - paint.ascent()

            // Ajouter un espace entre les questions
            y += 10f
        }
        pdfDocument.finishPage(page)

        val filePath = File(getExternalFilesDir(null), "ResultatsQuiz.pdf")
        try {
            pdfDocument.writeTo(FileOutputStream(filePath))
        } catch (e: IOException) {
            e.printStackTrace()
        }
        pdfDocument.close()

        val pdfFile = File(getExternalFilesDir(null), "ResultatsQuiz.pdf")
        val contentUri = FileProvider.getUriForFile(this, "tn.esprit.greenworld.fileprovider", pdfFile)

        val imgPdfAfficher = findViewById<ImageView>(R.id.img_pdf_afficher)
        imgPdfAfficher.setOnClickListener {
            try {
                val viewIntent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(contentUri, "application/pdf")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                startActivity(viewIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, "Aucune application pour ouvrir le PDF", Toast.LENGTH_SHORT).show()
            }
        }

        val imgPdfPartager = findViewById<ImageView>(R.id.img_pdf_partager)
        imgPdfPartager.setOnClickListener {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, contentUri)
                type = "application/pdf"
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(Intent.createChooser(shareIntent, "Partager le PDF"))
        }
    }

    private fun updateScore(userId: String, newScore: Int) {
        Log.d("ResultActivity", "Updating score for userID: $userId with new score: $newScore")

        RetrofitImp.buildRetrofit().create(UserInterface::class.java)
            .updateScoreById(User4( userId,newScore))
            .enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        handleSuccessfulRegistration(response.body())
                    } else {
                        handleRegistrationFailure(response.errorBody()?.string().toString())
                    } // Handle the response if needed
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    // Handle the failure if needed
                }
            })
    }
    private fun handleSuccessfulRegistration(user: User?) {
        // Handle successful registration
        // Optionally, you can navigate to the login screen

    }

    private fun handleRegistrationFailure(errorMessage: String) {
        // Handle registration failure
        Toast.makeText(
            this@ResultActivity,
            "Error: baabbababababababba",
            Toast.LENGTH_SHORT
        ).show()
    }

}