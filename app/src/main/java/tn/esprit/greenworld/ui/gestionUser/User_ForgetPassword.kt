package tn.esprit.greenworld.ui.gestionUser

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.greenworld.databinding.ActivityUserForgetPasswordBinding
import tn.esprit.greenworld.models.User
import tn.esprit.greenworld.models.User3
import tn.esprit.greenworld.utils.Login
import tn.esprit.greenworld.utils.RetrofitImp

class User_ForgetPassword : AppCompatActivity() {
    private lateinit var binding: ActivityUserForgetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnForgetPassword.setOnClickListener {
            RetrofitImp.buildRetrofit().create(Login::class.java).sendResetCode(
                User3(
                    email = binding.editTextEmail.text.toString(),

                )
            ).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        // Handle the successful response from the server
                        // For example, show a success message or navigate to another screen
                        val user = response.body()
                        user?.let {
                            // Pass the user data to UserProfil activity
                            Log.d("LoginActivity", "Login successful. User data: $user")

                            val intent = Intent(this@User_ForgetPassword, User_Validation::class.java)
                            intent.putExtra("userId", it._id)
                            intent.putExtra("resetCode", it.resetCode)
                            intent.putExtra("userEmail", it.email)

                            startActivity(intent)
                            finish()
                        } ?: run {
                            // Handle the case where user is null (optional)
                            Log.e("LoginActivity", "User data is null")

                            Toast.makeText(
                                this@User_ForgetPassword,
                                "User NOt Found ",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    } else {
                        // Handle the response in case of an error
                        // For example, show an error message with the specific error code
                        Toast.makeText(
                            this@User_ForgetPassword,
                            "Error: ${response.code()} - ${response.errorBody()?.string()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    // Handle the failure of the request
                    // For example, show a network error message
                    Toast.makeText(
                        this@User_ForgetPassword,
                        "Network error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }
}
