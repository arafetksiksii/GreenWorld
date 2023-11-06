package tn.esprit.greenworld.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import tn.esprit.greenworld.R
import tn.esprit.greenworld.databinding.ActivityUserRegisterBinding

class User_Register : AppCompatActivity() {

    private lateinit var binding: ActivityUserRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the root view to be used as the parent for Snackbar
        val rootView = window.decorView.rootView

        // Initialize the TextWatcher for the input fields
        initTextWatchers()

        // Handle Sign Up button click
        binding.btnSignUp.setOnClickListener {
            if (validateForm()) {
                // If validation is successful, navigate to the LoginActivity.
                startActivity(Intent(this, LoginActivity::class.java))
            } else {
                // If validation fails, show a Snackbar with an error message.
                Snackbar.make(rootView, getString(R.string.msg_error_inputs), Snackbar.LENGTH_SHORT).show()
            }
        }

        // Handle Terms and Policy button click
        binding.btnTermsAndPolicy.setOnClickListener {
            Snackbar.make(rootView, getString(R.string.msg_coming_soon), Snackbar.LENGTH_SHORT).show()
        }

        // Handle Return button click
        binding.btnReturn.setOnClickListener {
            finish()
        }
    }

    private fun initTextWatchers() {
        // TextWatcher for Full Name input
        binding.tiFullName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateFullName()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // TextWatcher for Email input
        binding.tiEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateEmail()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // TextWatcher for Password input
        binding.tiPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validatePassword()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // TextWatcher for Confirm Password input
        binding.tiConfirmPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateConfirmPassword()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun validateFullName(): Boolean {
        val fullName = binding.tiFullName.text.toString()
        binding.tiFullNameLayout.error = null

        if (fullName.isEmpty()) {
            binding.tiFullNameLayout.error = getString(R.string.msg_must_not_be_empty)
            return false
        }

        if (fullName.length < 6) {
            binding.tiFullNameLayout.error = getString(R.string.msg_check_your_characters)
            return false
        }

        return true
    }

    private fun validateEmail(): Boolean {
        val email = binding.tiEmail.text.toString()
        binding.tiEmailLayout.error = null

        if (email.isEmpty()) {
            binding.tiEmailLayout.error = getString(R.string.msg_must_not_be_empty)
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tiEmailLayout.error = getString(R.string.msg_check_your_email)
            return false
        }

        return true
    }

    private fun validatePassword(): Boolean {
        val password = binding.tiPassword.text.toString()
        binding.tiPasswordLayout.error = null

        if (password.isEmpty()) {
            binding.tiPasswordLayout.error = getString(R.string.msg_must_not_be_empty)
            return false
        }

        if (password.length < 6) {
            binding.tiPasswordLayout.error = getString(R.string.msg_check_your_characters)
            return false
        }

        return true
    }

    private fun validateConfirmPassword(): Boolean {
        val confirmPassword = binding.tiConfirmPassword.text.toString()
        val password = binding.tiPassword.text.toString()
        binding.tiConfirmPasswordLayout.error = null
        binding.tiPasswordLayout.error = null

        if (confirmPassword.isEmpty()) {
            binding.tiConfirmPasswordLayout.error = getString(R.string.msg_must_not_be_empty)
            return false
        }

        if (confirmPassword.length < 6) {
            binding.tiConfirmPasswordLayout.error = getString(R.string.msg_check_your_characters)
            return false
        }

        if (confirmPassword != password) {
            binding.tiConfirmPasswordLayout.error = getString(R.string.msg_check_your_confirm_Password)
            binding.tiPasswordLayout.error = getString(R.string.msg_check_your_confirm_Password)
            return false
        }

        return true
    }

    private fun validateForm(): Boolean {
        return validateFullName() && validateEmail() && validatePassword() && validateConfirmPassword()
    }
}
