package tn.esprit.greenworld.ui.gestionUser

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.greenworld.databinding.ActivityUserValidationBinding
import tn.esprit.greenworld.models.User
import tn.esprit.greenworld.models.User3
import tn.esprit.greenworld.utils.Login
import tn.esprit.greenworld.utils.RetrofitImp

class User_Validation : AppCompatActivity() {
    private lateinit var textViewEmail: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityUserValidationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserValidationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("user_pref", Context.MODE_PRIVATE)

        val userEmail = sharedPreferences.getString("userEmail", "")
        textViewEmail = binding.textView4
        textViewEmail.text = userEmail

        binding.btnValidation.setOnClickListener {
            startActivity(Intent(this, User_ValidationCode::class.java))
        }



        binding.btnCreateAccount.setOnClickListener {
            RetrofitImp.buildRetrofit().create(Login::class.java).sendResetCode(
                User3(email = userEmail.toString())
            ).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        val user = response.body()
                        Log.d("User_Validation",user.toString())
                        user?.let {
                            // Save values to shared preferences
                            val sharedPreferences =
                                getSharedPreferences("user_data", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putString("userId", it._id)
                            editor.putString("resetCode", it.resetCode)
                            editor.putString("userEmail", it.email)
                            editor.putString("token", it.token)
                            Log.d("token",it.token.toString())
                            editor.apply()
                        } ?: run {
                            Toast.makeText(
                                this@User_Validation,
                                "User Not Found",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        // Handle the response in case of an error
                        Toast.makeText(
                            this@User_Validation,
                            "Error: ${response.code()} - ${response.errorBody()?.string()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    // Handle the failure case
                    Toast.makeText(
                        this@User_Validation,
                        "Network Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
        binding.btnReturn.setOnClickListener {
            finish()
        }
    }


}