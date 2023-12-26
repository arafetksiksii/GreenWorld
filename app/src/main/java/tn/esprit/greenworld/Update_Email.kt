package tn.esprit.greenworld

import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import tn.esprit.greenworld.databinding.ActivityUpdateEmailBinding

class Update_Email : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateEmailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)
// Add an animation to the button
        val translateY = ObjectAnimator.ofFloat(binding.btnUpdateE, View.TRANSLATION_Y, 0f, -20f, 0f)
        translateY.duration = 500 // Set the duration of the animation
        translateY.interpolator = AccelerateDecelerateInterpolator() // Set the animation interpolator
        translateY.repeatCount = ObjectAnimator.INFINITE // Make the animation repeat infinitely
        translateY.start()
        binding.btnUpdateE.setOnClickListener {
            showPasswordDialog()
        }
    }

    private fun showPasswordDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Enter Your Password")

        val input = androidx.appcompat.widget.AppCompatEditText(this)
        input.transformationMethod = android.text.method.PasswordTransformationMethod.getInstance()
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, _ ->
            val password = input.text.toString()

            if (isValidPassword(password)) {
                // Password is valid, perform navigation here
                // Example: startActivity(Intent(this, NextActivity::class.java))
            } else {
                // Invalid password, display an error Snackbar
                Snackbar.make(binding.root, "Incorrect password", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(ContextCompat.getColor(this, R.color.colorPrimary))
                    .show()
            }
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun isValidPassword(password: String): Boolean {
        // Implement your password validation logic here
        // For example, check length, special characters, etc.
        return password.length >= 8
    }
}
