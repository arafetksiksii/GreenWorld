package tn.esprit.greenworld.ui.gestionUser

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.greenworld.R
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

        // Disable the button initially
        binding.btnForgetPassword.isEnabled = false

        // Add a listener to enable the button when the email EditText is not empty
        binding.editTextEmail.doAfterTextChanged { text ->
            binding.btnForgetPassword.isEnabled = text?.isNotEmpty() == true
        }

        // Add an animation to the button
        binding.btnForgetPassword.setOnClickListener {
            val inflater = layoutInflater
            val layout = inflater.inflate(R.layout.custom_toast, findViewById(R.id.custom_toast_layout))

            RetrofitImp.buildRetrofit().create(Login::class.java).sendResetCode(
                User3(email = binding.editTextEmail.text.toString())
            ).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        val user = response.body()
                        Log.d("ssssssssssssssssss", user.toString())
                        user?.let {
                            // Save values to shared preferences
                            val sharedPreferences =
                                getSharedPreferences("user_data", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putString("userId", it._id)
                            editor.putString("resetCode", it.resetCode)
                            editor.putString("userEmail", it.email)
                            editor.putString("token", it.token)
                            Log.d("token", it.token.toString())
                            editor.apply()

                            // Start the User_Validation activity
                            // Start the User_Validation activity and pass the email to it
                            val intent = Intent(this@User_ForgetPassword, User_Validation::class.java)
                            intent.putExtra("userEmail", it.email)
                            startActivity(intent)
                            finish()
                        } ?: run {
                            val text = layout.findViewById<TextView>(R.id.custom_toast_text)
                            text.text = "User Not Found"

                            val toast = Toast(applicationContext)
                            toast.setGravity(Gravity.BOTTOM, 0, 16)
                            toast.duration = Toast.LENGTH_LONG
                            toast.view = layout
                            toast.show()
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
                    val text = layout.findViewById<TextView>(R.id.custom_toast_text)
                    text.text = "This is a custom toast message"

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
