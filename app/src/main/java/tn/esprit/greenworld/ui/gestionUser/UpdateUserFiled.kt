package tn.esprit.greenworld.ui.gestionUser

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.greenworld.MIDrawerActivity
import tn.esprit.greenworld.databinding.ActivityUpdateUserFiledBinding
import tn.esprit.greenworld.models.User
import tn.esprit.greenworld.models.User5
import tn.esprit.greenworld.utils.RetrofitImp
import tn.esprit.greenworld.utils.UserInterface

class UpdateUserFiled : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateUserFiledBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateUserFiledBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("user_pref", MODE_PRIVATE)
        binding.btnReturn.setOnClickListener {
            finish()
        }
        binding.buttonUpdate.setOnClickListener { updateUserp() }
    }

    private fun updateUserp() {
        val userId = sharedPreferences.getString("userId", "")

        val user5 = User5(
            id = userId ?: "",
            email = "", // Assuming you will get this from somewhere or leave it as an empty string
            nom = binding.editTextLastName.text.toString(),
            prenom = binding.editTextFirstName.text.toString(),
            userName = binding.editTextUsername.text.toString(),
            adress = "", // Assuming you will get this from somewhere or leave it as an empty string
            numTel = binding.editTextPhoneNumber.text.toString(),
            imageRes = "" // Assuming you will get this from somewhere or leave it as an empty string
        )

        val imageService = RetrofitImp.buildRetrofit().create(UserInterface::class.java)
        imageService.updateUser(user5).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    handleSuccessfulupdate(response.body())
                } else {
                    handleUpdateFailure(response.errorBody()?.string().toString())
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                handleUpdateFailure(t.message ?: "Unknown error")
            }
        })
    }

    private fun handleSuccessfulupdate(user: User?) {
        if (user != null) {
         //   saveUserToPreferences(user)
           // startActivity(Intent(this@UpdateUserFiled, MIDrawerActivity::class.java))
            //finish()
            showSnackbar(" updating user informationsucces")
        } else {
            Log.e("UpdateUserFiled", "Received null user in handleSuccessfulupdate")
            showSnackbar("Error updating user information")
        }
    }

    private fun handleUpdateFailure(errorMessage: String) {
        showSnackbar(errorMessage)
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun saveUserToPreferences(user: User) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("password", user.password)
        editor.putString("userId", user._id)
        editor.putString("userName", user.userName)
        editor.putString("userEmail", user.email)
        editor.putString("userImageRes", user.imageRes)
        editor.putString("tokenLogin", user.token)
        editor.putString("userNBLogin", user.loginCount)
        editor.putString("userTimePass", user.totalTimeSpent)
        editor.putString("userScore", user.score.toString())

        editor.apply()

        Log.d(
            "UpdateUserFiled",
            "Saved UserID: ${user._id}, UserName: ${user.userName}, UserEmail: ${user.email}, UserImageRes: ${user.imageRes}, UserToken: ${user.token}"
        )
    }
}
