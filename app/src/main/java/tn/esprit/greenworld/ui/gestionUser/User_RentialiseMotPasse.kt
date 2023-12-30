package tn.esprit.greenworld.ui.gestionUser

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.greenworld.R
import tn.esprit.greenworld.databinding.ActivityUserRentialiseMotPasseBinding
import tn.esprit.greenworld.models.User
import tn.esprit.greenworld.models.User1
import tn.esprit.greenworld.models.User2
import tn.esprit.greenworld.models.User5
import tn.esprit.greenworld.utils.RetrofitImp
import tn.esprit.greenworld.utils.UserInterface

class User_RentialiseMotPasse:AppCompatActivity() {

   private lateinit var binding: ActivityUserRentialiseMotPasseBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = ActivityUserRentialiseMotPasseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSubmit.setOnClickListener {
            if (validatePassword() && validateConfirmPassword()) {
                // Update user information
                updateUser()
            }



                }
        binding.btnReturn.setOnClickListener {
            finish()
        }

    }


    private fun validatePassword(): Boolean {
        binding.tiPasswordLayout.isErrorEnabled = false

        if (binding.tiPassword.text.toString().isEmpty()) {
            binding.tiPasswordLayout.error = getString(R.string.msg_must_not_be_empty)
            binding.tiPassword.requestFocus()
            return false
        }else{
            binding.tiPasswordLayout.isErrorEnabled = false
        }

        if (binding.tiPassword.text.toString().length < 6) {
            binding.tiPasswordLayout.error = getString(R.string.msg_check_your_characters)
            binding.tiPassword.requestFocus()
            return false
        }else{
            binding.tiPasswordLayout.isErrorEnabled = false
        }

        return true
    }

    private fun validateConfirmPassword(): Boolean {
        binding.tiConfirmPasswordLayout.isErrorEnabled = false

        if (binding.tiConfirmPassword.text.toString().isEmpty()) {
            binding.tiConfirmPasswordLayout.error = getString(R.string.msg_must_not_be_empty)
            binding.tiConfirmPassword.requestFocus()
            return false
        }else{
            binding.tiConfirmPasswordLayout.isErrorEnabled = false
        }

        if (binding.tiConfirmPassword.text.toString().length < 6) {
            binding.tiConfirmPasswordLayout.error = getString(R.string.msg_check_your_characters)
            binding.tiConfirmPassword.requestFocus()
            return false
        }else{
            binding.tiConfirmPasswordLayout.isErrorEnabled = false
        }

        if (!binding.tiConfirmPassword.text.toString().equals(binding.tiPassword.text.toString())) {
            binding.tiConfirmPasswordLayout.error = getString(R.string.msg_check_your_confirm_Password)
            binding.tiPasswordLayout.error = getString(R.string.msg_check_your_confirm_Password)
            binding.tiConfirmPassword.requestFocus()
            return false
        }else{
            binding.tiConfirmPasswordLayout.isErrorEnabled = false
            binding.tiPasswordLayout.isErrorEnabled = false
        }

        return true
    }


    private fun updateUser() {
        // Get user data from SharedPreferences
        // val sharedPreferences: SharedPreferences = getSharedPreferences("user_pref", MODE_PRIVATE)

        val sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("token", "")

        Log.d("hhhhhhhhhhh",userId.toString())
        val user1 = User1(
            email = userId?.toString() ?: "", // Assurez-vous que userId n'est pas null
            password= binding.tiConfirmPassword.text.toString())

        val update = RetrofitImp.buildRetrofit().create(UserInterface::class.java)
        update.updateUser2(user1).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    handleSuccessfulupdate(response.body())
                } else {
                    handleUpdateFailure(response.errorBody()?.string().toString())
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                // Handle the failure here
                // You can log the error or show a message to the user
                handleUpdateFailure(t.message ?: "Unknown error")
            }
        })
    }
    private fun handleSuccessfulupdate(user: User?) {
        // Handle successful registration
        // Optionally, you can navigate to the login screen
        startActivity(Intent(this@User_RentialiseMotPasse, LoginActivity::class.java))
        finish()
    }

    private fun handleUpdateFailure(errorMessage: String) {
        // Handle registration failure
        showSnackbar(errorMessage)
    }
    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}