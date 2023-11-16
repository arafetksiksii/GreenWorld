package tn.esprit.greenworld.ui.gestionUser

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.greenworld.R
import tn.esprit.greenworld.databinding.ActivityUserLoginBinding
import tn.esprit.greenworld.models.User
import tn.esprit.greenworld.models.User1
import tn.esprit.greenworld.utils.Login
import tn.esprit.greenworld.utils.RetrofitImp


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val rootView = window.decorView.rootView

        binding.btnLogin.setOnClickListener {

            RetrofitImp.buildRetrofit().create(Login::class.java).login(
                User1(
                    email = binding.edtEmail.text.toString(),
                    password = binding.tiPassword.text.toString()
                )
            ).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        val user = response.body()

                        // Check if user is not null before accessing its properties
                        user?.let {
                            // Pass the user data to UserProfil activity
                            Log.d("LoginActivity", "Login successful. User data: $user")

                            val intent = Intent(this@LoginActivity, UserProfil::class.java)
                            intent.putExtra("userId", it._id)
                            intent.putExtra("userName", it.userName)
                            intent.putExtra("userEmail", it.email)
                            intent.putExtra("userImageRes", it.imageRes)
                            startActivity(intent)
                            finish()
                        } ?: run {
                            // Handle the case where user is null (optional)
                            Log.e("LoginActivity", "User data is null")

                            Snackbar.make(
                                binding.contextView,
                                "User data is null",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        val errorBody = response.errorBody()?.string().toString()
                        Log.d("LoginActivity", "Login failed. Error: $errorBody")

                        Snackbar.make(
                            binding.contextView,
                            errorBody,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.e("LoginActivity", "Login request failed", t)

                    Snackbar.make(
                        binding.contextView,
                        t.message.toString(),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            })


        }

        binding.btnCreateAccount.setOnClickListener {
            startActivity(Intent(this, User_Register::class.java))
        }
    }

    private fun validateEmail(): Boolean {
        binding.tiEmailLayout.isErrorEnabled = false

        if (binding.edtEmail.text.toString().isEmpty()) {
            binding.tiEmailLayout.error = getString(R.string.msg_must_not_be_empty)
            binding.edtEmail.requestFocus()
            return false
        } else {
            binding.tiEmailLayout.isErrorEnabled = false
        }

        if (Patterns.EMAIL_ADDRESS.matcher(binding.edtEmail.text.toString()).matches()) {
            binding.tiEmailLayout.error = getString(R.string.msg_check_your_email)
            binding.edtEmail.requestFocus()
            return false
        } else {
            binding.tiEmailLayout.isErrorEnabled = false
        }

        return true
    }

    private fun validatePassword(): Boolean {
        binding.tiPasswordLayout.isErrorEnabled = false

        if (binding.tiPassword.text.toString().isEmpty()) {
            binding.tiPasswordLayout.error = getString(R.string.msg_must_not_be_empty)
            binding.tiPassword.requestFocus()
            return false
        } else {
            binding.tiPasswordLayout.isErrorEnabled = false
        }

        if (binding.tiPassword.text.toString().length < 6) {
            binding.tiPasswordLayout.error = getString(R.string.msg_check_your_characters)
            binding.tiPassword.requestFocus()
            return false
        } else {
            binding.tiPasswordLayout.isErrorEnabled = false
        }

        return true
    }
}