package tn.esprit.greenworld.ui.gestionUser

import android.content.Context
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
                User3(email = binding.editTextEmail.text.toString())
            ).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        val user = response.body()
                        Log.d("ssssssssssssssssss",user.toString())
                        user?.let {
                            // Save values to shared preferences
                            val sharedPreferences =
                                getSharedPreferences("user_data", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putString("userId", it._id)
                            editor.putString("resetCode", it.resetCode)
                            editor.putString("userEmail", it.email)
                            editor.apply()

                            // Start the User_Validation activity
                            startActivity(Intent(this@User_ForgetPassword, User_Validation::class.java))
                            finish()
                        } ?: run {
                            Toast.makeText(
                                this@User_ForgetPassword,
                                "User Not Found",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        // Handle the response in case of an error
                        Toast.makeText(
                            this@User_ForgetPassword,
                            "Error: ${response.code()} - ${response.errorBody()?.string()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    // Handle the failure case
                    Toast.makeText(
                        this@User_ForgetPassword,
                        "Network Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

    }
}
