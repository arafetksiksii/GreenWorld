package tn.esprit.greenworld

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.greenworld.databinding.ActivityUpdatePasswordBinding
import tn.esprit.greenworld.models.User
import tn.esprit.greenworld.models.User1
import tn.esprit.greenworld.models.User3
import tn.esprit.greenworld.utils.Login
import tn.esprit.greenworld.utils.RetrofitImp
import tn.esprit.greenworld.utils.UserInterface

class Update_Password : AppCompatActivity() {
    private lateinit var binding: ActivityUpdatePasswordBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdatePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("user_pref", Context.MODE_PRIVATE)

        val userEmail = sharedPreferences.getString("userEmail", "")
        binding.btnUpdate.setOnClickListener {

            val password1 = binding.tiPassword.text.toString()
            val password2 = binding.tiPassword2.text.toString()

            // Check if tiPassword2 is equal to tiPassword
            if (password1 == password2 && isValidPassword(password1)) {
                RetrofitImp.buildRetrofit().create(Login::class.java).sendResetCode(
                    User3(email = userEmail.toString())
                ).enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if (response.isSuccessful) {
                            val user = response.body()
                            Log.d("User_ValidationCode", user.toString())
                            user?.let {
                                // Save values to shared preferences

                                showRestCodeDialog(it.resetCode.toString(),password1,userEmail.toString())

                            } ?: run {
                                Toast.makeText(
                                    this@Update_Password,
                                    "User Not Found",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            // Handle the response in case of an error
                            Toast.makeText(
                                this@Update_Password,
                                "Error: ${response.code()} - ${response.errorBody()?.string()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        // Handle the failure case
                        Toast.makeText(
                            this@Update_Password,
                            "Network Error: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
                // Show a dialog to enter the rest code

            } else {
                // Passwords are not equal, show a Snackbar with an error message
                Snackbar.make(
                    binding.root,
                    "Passwords do not match",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        // Handle Return button click
        binding.btnReturn.setOnClickListener {
            finish()
        }
    }

    private fun showRestCodeDialog(code: String,password: String,email: String) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_rest_code, null)
        val etRestCode = dialogView.findViewById<EditText>(R.id.etRestCode)

        // Build the AlertDialog
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Enter Rest Code")
            .setView(dialogView)
            .setPositiveButton("Submit") { dialog, _ ->
                val restCode = etRestCode.text.toString()
                if (isValidRestCode(restCode) && restCode == code) {
                    // Valid rest code, perform profile update
                    updateProfile(password,email)
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

    private fun updateProfile(password: String, email: String) {
        // TODO: Implement your logic for updating the profile
        // Example: Update user profile on the server

        // Create an instance of the UserInterface
        val userInterface = RetrofitImp.buildRetrofit().create(UserInterface::class.java)

        // Call the updatePassword function with the specified parameters
        userInterface.updatePassword(User1(email = email, password = password)).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    // Profile update successful
                    navigateToProfile()

                    // Show a Snackbar for password update success
                    showSnackbar("Password updated successfully")
                } else {
                    // Handle the response in case of an error
                    Toast.makeText(
                        this@Update_Password,
                        "Error: ${response.code()} - ${response.errorBody()?.string()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                // Handle the failure case
                Toast.makeText(
                    this@Update_Password,
                    "Network Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }



    private fun navigateToProfile() {
        // TODO: Implement navigation to the profile activity
        // Example:
        // val intent = Intent(this, ProfileActivity::class.java)
        // startActivity(intent)
    }
    private fun isValidPassword(password: String): Boolean {
        // Add your password validation logic here
        val passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}\$"
        return Regex(passwordRegex).matches(password)
    }
}
