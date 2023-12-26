// Update_Password.kt

package tn.esprit.greenworld

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import tn.esprit.greenworld.databinding.ActivityUpdatePasswordBinding

class Update_Password : AppCompatActivity() {
    private lateinit var binding: ActivityUpdatePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdatePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnUpdate.setOnClickListener {
            val password1 = binding.tiPassword.text.toString()
            val password2 = binding.tiPassword2.text.toString()

            // Check if tiPassword2 is equal to tiPassword
            if (password1 == password2) {
                // Show a dialog to enter the rest code
                showRestCodeDialog()
            } else {
                // Passwords are not equal, show a Snackbar with an error message
                Snackbar.make(
                    binding.root,
                    "Passwords do not match",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showRestCodeDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_rest_code, null)
        val etRestCode = dialogView.findViewById<EditText>(R.id.etRestCode)

        // Build the AlertDialog
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Enter Rest Code")
            .setView(dialogView)
            .setPositiveButton("Submit") { dialog, _ ->
                val restCode = etRestCode.text.toString()
                if (isValidRestCode(restCode)) {
                    // Valid rest code, perform profile update
                    updateProfile()
                } else {
                    // Invalid rest code, show a Snackbar with an error message
                    Snackbar.make(
                        binding.root,
                        "Invalid Rest Code",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        // Show the dialog
        alertDialog.show()
    }

    // Example function to validate the rest code (replace with your own validation logic)
    private fun isValidRestCode(restCode: String): Boolean {
        return restCode.isNotEmpty()
    }






    private fun updateProfile() {
        // TODO: Implement your logic for updating the profile
        // Example: Update user profile on the server

        // After updating the profile, navigate to the ProfileActivity
        navigateToProfile()
    }

    private fun navigateToProfile() {
        // TODO: Implement navigation to the profile activity
        // Example:
        // val intent = Intent(this, ProfileActivity::class.java)
        // startActivity(intent)
    }
}
