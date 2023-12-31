package tn.esprit.greenworld.ui.gestionUser


import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import com.facebook.CallbackManager
import com.facebook.FacebookAuthorizationException
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookOperationCanceledException
import com.facebook.FacebookSdk
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.greenworld.MIDrawerActivity
import tn.esprit.greenworld.databinding.ActivityUserLoginBinding
import tn.esprit.greenworld.models.User
import tn.esprit.greenworld.models.User1
import tn.esprit.greenworld.models.UserData
import tn.esprit.greenworld.utils.DatabaseHelper
import tn.esprit.greenworld.utils.Login
import tn.esprit.greenworld.utils.RetrofitImp


class LoginActivity : MIDrawerActivity() {
    private lateinit var binding: ActivityUserLoginBinding

    private lateinit var dbHelper: DatabaseHelper


    // Define a shared preference name
    private val PREF_NAME = "user_pref"
    private val USER_ID_KEY = "userId"
    private val USER_NAME_KEY = "userName"
    private val USER_EMAIL_KEY = "userEmail"
    private val USER_IMAGE_KEY = "userImageRes"
    private val USER_TOKEN_KEY = "tokenLogin"
    private val USER_TIMEPass_KEY = "userTimePass"
    private val USER_NBLOGIN_KEY = "userNBLogin"
    private val USER_SCORE_KEY = "userScore"
    private val PASSWORD_KEY_NEW = "new_password"
    private val USER_EMAIL_KEY_NEW = "new_email"


    private val REMEMBER_ME_KEY_NEW = "rememberMe"

    private val PASSWORD_KEY = "password"

    private lateinit var gso: GoogleSignInOptions
    private lateinit var gsc: GoogleSignInClient

    private lateinit var callbackManager: CallbackManager
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        binding = ActivityUserLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FacebookSdk.sdkInitialize(applicationContext)

        binding.btnLogin.setOnClickListener {
            val storedUsername = sharedPreferences.getString(USER_EMAIL_KEY_NEW, "")
            val storedPassword = sharedPreferences.getString(PASSWORD_KEY_NEW, "")

            // Check if "Remember Me" is enabled
            if (binding.btnRememberMe.isChecked) {
                val username = binding.edtEmail.text.toString()
                val password = binding.tiPassword.text.toString()

<<<<<<< Updated upstream
                // Save the entered credentials in SharedPreferences
                with(sharedPreferences.edit()) {
                    putString(USER_EMAIL_KEY_NEW, username)
                    putString(PASSWORD_KEY_NEW, password)
                    apply()
=======
                        // Check if user is not null before accessing its properties
                        user?.let {
                            // Pass the user data to UserProfil activity
                            saveUserToPreferences(it)
                            Log.d("LoginActivity", "Login successful. User data: $user")
                            Log.d("bbbbbbbbb",it._id)


                            val intent = Intent(this@LoginActivity, UserUpdate::class.java)
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
>>>>>>> Stashed changes
                }

                performLogin(username, password)
            } else if (!storedUsername.isNullOrBlank() && !storedPassword.isNullOrBlank()) {
                // "Remember Me" is not checked, but stored credentials are available
                // Pre-fill login fields with stored credentials
                binding.edtEmail.setText(storedUsername)
                binding.tiPassword.setText(storedPassword)

                // Perform login with stored credentials
                performLogin(storedUsername, storedPassword)
            } else {
                // "Remember Me" is not checked, and no stored credentials
                // Perform login with user-entered credentials
                val username = binding.edtEmail.text.toString()
                val password = binding.tiPassword.text.toString()

                if (username.isNotBlank() && password.isNotBlank()) {
                    performLogin(username, password)
                } else {
                    // Display a Toast message if username or password is empty
                    Toast.makeText(
                        this@LoginActivity,
                        "Please enter both username and password.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }




        binding.btnCreateAccount.setOnClickListener {
            startActivity(Intent(this, User_Register::class.java))
        }

        // Add an animation to the button
        val translateY =
            ObjectAnimator.ofFloat(binding.btnForgotPassword, View.TRANSLATION_Y, 0f, -20f, 0f)
        translateY.duration = 500 // Set the duration of the animation
        translateY.interpolator =
            AccelerateDecelerateInterpolator() // Set the animation interpolator
        translateY.repeatCount = ObjectAnimator.INFINITE // Make the animation repeat infinitely
        translateY.start()


        // Stop the animation when the user starts inputting email
        binding.edtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Check if the email field is not empty and cancel the animation
                if (!s.isNullOrEmpty()) {
                    translateY.cancel()
                }
            }
        })
        binding.btnForgotPassword.setOnClickListener {

            startActivity(Intent(this, User_ForgetPassword::class.java))

        }

        dbHelper = DatabaseHelper(this)
        dbHelper.readableDatabase // Open the database

        binding.btnRememberMe.setOnCheckedChangeListener { _, isChecked ->
            with(sharedPreferences.edit()) {
                putBoolean(REMEMBER_ME_KEY_NEW, isChecked)

                val username = binding.edtEmail.text.toString()
                val password = binding.tiPassword.text.toString()

                if (isChecked) {
                    // Save the credentials in shared preferences with new key names
                    putString(USER_EMAIL_KEY_NEW, username)
                    putString(PASSWORD_KEY_NEW, password)
                } else {
                    // Clear the credentials from shared preferences with new key names
                    remove(USER_EMAIL_KEY_NEW)
                    remove(PASSWORD_KEY_NEW)
                }

                apply()
            }
        }



        gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        gsc = GoogleSignIn.getClient(this, gso)

        binding.btnGoogleLogin.setOnClickListener {
            signIn()

        }


        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    val accessToken = loginResult.accessToken
                    val request = GraphRequest.newMeRequest(accessToken) { jsonObject, _ ->
                        jsonObject?.let {
                            try {
                                val userId = it.getString("id")
                                val userName = it.getString("name")
                                val userEmail = it.getString("email")

                                // Print or use the user information as needed
                                Log.d("FacebookLogin", "User ID: $userId, Name: $userName, Email: $userEmail")

                                // You can pass this information to other functions or activities as needed
                                // For example, save it to SharedPreferences or start a new activity
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        }
                    }

                    val parameters = Bundle()
                    parameters.putString("fields", "id,name,email") // Specify the fields you need
                    request.parameters = parameters
                    request.executeAsync()
                }



                override fun onCancel() {
                    // App code
                }

                override fun onError(exception: FacebookException) {
                    // Handle different types of FacebookException
                    when (exception) {
                        is FacebookAuthorizationException -> {
                            // Authorization failed (e.g., user denied permissions)
                            Log.e("FacebookLogin", "Authorization failed: ${exception.message}")
                            // Handle the error in your app, show a message to the user, etc.
                        }
                        is FacebookOperationCanceledException -> {
                            // Operation canceled (e.g., the user canceled the login process)
                            Log.e("FacebookLogin", "Operation canceled: ${exception.message}")
                            // Handle the error in your app, show a message to the user, etc.
                        }
                        else -> {
                            // Other types of FacebookException
                            Log.e("FacebookLogin", "Facebook login error: ${exception.message}")
                            // Handle the error in your app, show a generic message to the user, etc.
                        }
                    }
                }})
        binding.btnFacebookLogin.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile"))
        }



    }


    private fun saveUserToPreferences(user: User) {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString(PASSWORD_KEY, user.password)
        editor.putString(USER_ID_KEY, user._id)
        editor.putString(USER_NAME_KEY, user.userName)
        editor.putString(USER_EMAIL_KEY, user.email)
        editor.putString(USER_IMAGE_KEY, user.imageRes)
        editor.putString(USER_TOKEN_KEY, user.token)
        editor.putString(USER_NBLOGIN_KEY, user.loginCount)
        editor.putString(USER_TIMEPass_KEY, user.totalTimeSpent)
        editor.putString(USER_SCORE_KEY, user.score.toString())




        editor.apply()

        // Add logs to check values after saving
        Log.d(
            "UserProfileFragment",
            "Saved UserID: ${user._id}, UserName: ${user.userName}, UserEmail: ${user.email}, UserImageRes: ${user.imageRes}, UserToken: ${user.token}"
        )
    }


    // Extract the login logic into a separate function
    private fun performLogin(username: String, password: String) {
        RetrofitImp.buildRetrofit().create(Login::class.java).login(
            User1(
                email = username,
                password = password


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

                            val intent =
                                Intent(this@LoginActivity, MIDrawerActivity::class.java)
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



    private fun signIn() {
        val signInIntent = gsc!!.signInIntent
        startActivityForResult(signInIntent, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1000) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)

                val username = account?.displayName
                val email = account?.email
                val userId = account?.id

                Log.d("googem1", "$email$username$userId")

                RetrofitImp.buildRetrofit().create(Login::class.java).loginWithFacebook(
                    UserData(
                        email = email.orEmpty(),
                        tokenfb = userId.orEmpty(),
                        nom = username.orEmpty(),
                        prenom = username.orEmpty()
                    )
                ).enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if (response.isSuccessful) {
                            val user = response.body()
                            Log.d("user fbbbb0", user.toString())
                            user?.let {
                                Log.d("eeeeeeeeeee", user.toString())
                                saveUserToPreferences(user)

                                val intent = Intent(this@LoginActivity, MIDrawerActivity::class.java)
                                intent.putExtra("userId", user._id)
                                intent.putExtra("userName", user.userName)
                                intent.putExtra("userEmail", user.email)
                                intent.putExtra("userImageRes", user.imageRes)
                                startActivity(intent)
                                finish()
                            } ?: run {
                                Log.e("LoginActivity", "Login response body is null")
                                Snackbar.make(
                                    binding.contextView,
                                    "Login failed. Please try again.",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            val errorBody = response.errorBody()?.string().orEmpty()
                            Log.d("LoginActivity", "Login failed. Error: $errorBody")
                            Snackbar.make(
                                binding.contextView,
                                "Login failed. Please try again.",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Log.e("LoginActivity", "Login request failed", t)
                        Snackbar.make(
                            binding.contextView,
                            "Login request failed. Please try again.",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                })
            } catch (e: ApiException) {
                Toast.makeText(
                    applicationContext,
                    "Google Sign-In failed: ${e.statusCode}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }


}