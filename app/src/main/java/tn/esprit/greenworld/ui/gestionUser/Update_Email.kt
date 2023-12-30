package tn.esprit.greenworld.ui.gestionUser

import android.animation.ObjectAnimator
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.greenworld.R
import tn.esprit.greenworld.databinding.ActivityUpdateEmailBinding
import tn.esprit.greenworld.models.User
import tn.esprit.greenworld.models.User5
import tn.esprit.greenworld.utils.RetrofitImp
import tn.esprit.greenworld.utils.UserInterface

class Update_Email : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateEmailBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Add an animation to the button
        val translateY =
            ObjectAnimator.ofFloat(binding.btnUpdateE, View.TRANSLATION_Y, 0f, -20f, 0f)
        translateY.duration = 500 // Set the duration of the animation
        translateY.interpolator =
            AccelerateDecelerateInterpolator() // Set the animation interpolator
        translateY.repeatCount = ObjectAnimator.INFINITE // Make the animation repeat infinitely
        translateY.start()
        sharedPreferences = getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("userEmail", "")

        binding.btnUpdateE.setOnClickListener {
            val ide = sharedPreferences.getString("userId", "")
            Log.d("iddddddddd",ide.toString())
            // Get the values from EditTexts
            var currentEmail = binding.edtEmail.text.toString()
            val newEmail = binding.edtEmail2.text.toString()

            // Validate the new email
            if (currentEmail == userEmail.toString() && isValidEmail(newEmail)) {
                RetrofitImp.buildRetrofit().create(UserInterface::class.java).updateUser(
                    User5(email = newEmail, id = ide.toString())

                ).enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if (response.isSuccessful) {
                            showSnackbar("Email updated successfully")
                        } else {
                            showSnackbar("Email updated Failed")
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        showSnackbar("onFailure")
                    }
                })
                Log.d("iddddddddd",ide.toString())


            } else {
                // Show an error Snackbar for invalid email format
                showSnackbar("Invalid email format")
            }
        }

        // Handle Return button click
        binding.btnReturn.setOnClickListener {
            finish()
        }
    }

    private fun isValidEmail(email: String): Boolean {
        // Add your specific email validation logic here
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(ContextCompat.getColor(this, R.color.colorPrimary))
            .setTextColor(ContextCompat.getColor(this, R.color.colorTextPrimary))
            .show()
    }
}
