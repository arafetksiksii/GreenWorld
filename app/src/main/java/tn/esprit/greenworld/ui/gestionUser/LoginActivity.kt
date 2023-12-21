package tn.esprit.greenworld.ui.gestionUser


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.greenworld.MIDrawerActivity
import tn.esprit.greenworld.R
import tn.esprit.greenworld.UserUpdate
import tn.esprit.greenworld.databinding.ActivityUserLoginBinding
import tn.esprit.greenworld.models.User
import tn.esprit.greenworld.models.User1
import tn.esprit.greenworld.utils.Login
import tn.esprit.greenworld.utils.RetrofitImp


class LoginActivity : MIDrawerActivity() {
    private lateinit var binding: ActivityUserLoginBinding
    private lateinit var callbackManager: CallbackManager


    // Define a shared preference name
    private val PREF_NAME = "user_pref"
    private val USER_ID_KEY = "userId"
    private val USER_NAME_KEY = "userName"
    private val USER_EMAIL_KEY = "userEmail"
    private val USER_IMAGE_KEY = "userImageRes"
    private val USER_TOKEN_KEY = "tokenLogin"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val rootView = window.decorView.rootView
        callbackManager = CallbackManager.Factory.create()

        val loginButton = binding.loginButton
        loginButton.setReadPermissions("email", "public_profile")
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                // Handle successful login
                val intent = Intent(this@LoginActivity, MIDrawerActivity::class.java)
            }

            override fun onCancel() {
                // Handle canceled login
            }

            override fun onError(error: FacebookException) {
                // Handle login error
            }
        })

        binding.btnLogin.setOnClickListener {

            RetrofitImp.buildRetrofit().create(Login::class.java).login(
                User1(
                  // email = binding.edtEmail.text.toString(),
                   //password = binding.tiPassword.text.toString()
                          email = "cexepis601@kxgif.com",
                    password = "123456789"

                )
            ).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        val user = response.body()
                        if (user != null) {
                            Log.d("eeeeeeeeeee", user.toString())
                            // Access the "user" property directly
                            val userObject = user
                            if (userObject != null) {
                                // Pass the user data to UserProfil activity
                                saveUserToPreferences(userObject)
                                Log.d("LoginActivity", "Login successful. User data: $userObject")
                                Log.d("bbbbbbbbb", userObject.toString())

                                val intent = Intent(this@LoginActivity, MIDrawerActivity::class.java)
                                intent.putExtra("userId", userObject._id)
                                intent.putExtra("userName", userObject.userName)
                                intent.putExtra("userEmail", userObject.email)
                                intent.putExtra("userImageRes", userObject.imageRes)
                                startActivity(intent)
                                finish()
                            } else {
                                // Handle the case where the "user" property is null
                                Log.e("LoginActivity", "User object is null")
                               // val intent = Intent(this@LoginActivity, UserProfileFragment::class.java)

                                Snackbar.make(
                                    binding.contextView,
                                    "User object is null",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            // Handle the case where the entire response body is null
                            Log.e("LoginActivity", "Response body is null")

                            Snackbar.make(
                                binding.contextView,
                                "Response body is null",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        // Handle the case where the response is not successful
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
        binding.btnForgotPassword.setOnClickListener {

            startActivity(Intent(this, User_ForgetPassword::class.java))

        }

    }
    private fun validateEmail(): Boolean {
        val email = binding.edtEmail.text.toString().trim()

        if (email.isEmpty()) {
            binding.tiEmailLayout.error = getString(R.string.msg_must_not_be_empty)
            binding.edtEmail.requestFocus()
            return false
        } else {
            binding.tiEmailLayout.isErrorEnabled = false
        }

        // Improved email validation using Regex
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        if (!email.matches(emailRegex.toRegex())) {
            binding.tiEmailLayout.error = getString(R.string.msg_invalid_email)
            binding.edtEmail.requestFocus()
            return false
        } else {
            binding.tiEmailLayout.isErrorEnabled = false
        }

        return true
    }


    private fun validatePassword(): Boolean {
        val password = binding.tiPassword.text.toString().trim()

        if (password.isEmpty()) {
            binding.tiPasswordLayout.error = getString(R.string.msg_must_not_be_empty)
            binding.tiPassword.requestFocus()
            return false
        } else {
            binding.tiPasswordLayout.isErrorEnabled = false
        }

        if (password.length < 6) {
                binding.tiPasswordLayout.error = getString(R.string.msg_password_length)
                binding.tiPassword.requestFocus()
                return false
            } else {
            binding.tiPasswordLayout.isErrorEnabled = false
        }

        // Ajouter des critères de complexité, par exemple :
        // Exiger au moins une lettre majuscule
        if (!password.any { it.isUpperCase() }) {
            binding.tiPasswordLayout.error = getString(R.string.msg_password_uppercase)
            binding.tiPassword.requestFocus()
            return false
        } else {
            binding.tiPasswordLayout.isErrorEnabled = false
        }

        // Exiger au moins un chiffre
        if (!password.any { it.isDigit() }) {
            binding.tiPasswordLayout.error = getString(R.string.msg_password_digit)
            binding.tiPassword.requestFocus()
            return false
        } else {
            binding.tiPasswordLayout.isErrorEnabled = false
        }

        // Exiger au moins un caractère spécial
        val specialCharacters = "!@#$%^&*()-_+=<>?/{}|"
        if (!password.any { specialCharacters.contains(it) }) {
            binding.tiPasswordLayout.error = getString(R.string.msg_password_special_char)
            binding.tiPassword.requestFocus()
            return false
        } else {
            binding.tiPasswordLayout.isErrorEnabled = false
        }

        return true
    }
    private fun saveUserToPreferences(user: User) {
        val sharedPreferences: SharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString(USER_ID_KEY, user._id)
        editor.putString(USER_NAME_KEY, user.userName)
        editor.putString(USER_EMAIL_KEY, user.email)
        editor.putString(USER_IMAGE_KEY, user.imageRes)
        editor.putString(USER_TOKEN_KEY, user.token)
        editor.apply()

        // Add logs to check values after saving
        Log.d("UserProfileFragment", "Saved UserID: ${user._id}, UserName: ${user.userName}, UserEmail: ${user.email}, UserImageRes: ${user.imageRes}, UserToken: ${user.token}")
    }
    // New method for logout
    private fun logout() {
        val sharedPreferences: SharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Clear all saved user data from shared preferences
        editor.remove(USER_ID_KEY)
        editor.remove(USER_NAME_KEY)
        editor.remove(USER_EMAIL_KEY)
        editor.remove(USER_IMAGE_KEY)
        editor.remove(USER_TOKEN_KEY)

        // Apply changes
        editor.apply()

        // Redirect to the login screen or any other appropriate action
        val intent = Intent(this@LoginActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}