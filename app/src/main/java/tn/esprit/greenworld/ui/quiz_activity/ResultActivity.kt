package tn.esprit.greenworld.ui.quiz_activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.greenworld.R
import tn.esprit.greenworld.models.User
import tn.esprit.greenworld.models.User4
import tn.esprit.greenworld.ui.gestionUser.LoginActivity
import tn.esprit.greenworld.utils.RetrofitImp
import tn.esprit.greenworld.utils.UserInterface

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

        // Get user data from SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("user_pref", MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", "")

        // Make the API call to update the user's score
        userId?.let {
            updateScore(it, score)

        }

        val backButton = findViewById<ImageView>(R.id.img_back)
        backButton.setOnClickListener {
            // Create an intent to start a new activity
            val intent = Intent(this, QuizListActivity::class.java)
            // Start the activity
            startActivity(intent)
            // Optionally: if you don't want to keep the current activity in the activity stack
            finish()
        }
    }

    private fun updateScore(userId: String, newScore: Int) {
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
        startActivity(Intent(this@ResultActivity, LoginActivity::class.java))
        finish()
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