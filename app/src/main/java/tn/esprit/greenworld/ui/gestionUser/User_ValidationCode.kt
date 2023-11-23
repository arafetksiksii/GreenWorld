
package tn.esprit.greenworld.ui.gestionUser
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import tn.esprit.greenworld.databinding.ActivityUserValidationCodeBinding


class User_ValidationCode : AppCompatActivity() {

    private lateinit var binding: ActivityUserValidationCodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserValidationCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnVerify.setOnClickListener {
            if (checkOTP()) {
                // Verification code is correct
                startActivity(Intent(this, User_RentialiseMotPasse::class.java))
            } else {
                // Verification code is incorrect
                Toast.makeText(this, "Incorrect verification code", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnResendCode.setOnClickListener {
            // Handle resend code logic here
            // For example, you can resend the verification code to the user
        }

        setEditTextListeners()
    }

    private fun setEditTextListeners() {
        val editTextList = listOf(
            binding.tiCode1, binding.tiCode2, binding.tiCode3, binding.tiCode4
        )

        editTextList.forEachIndexed { index, editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1 && index < editTextList.size - 1) {
                        editTextList[index + 1].requestFocus()
                    } else if (s?.isEmpty() == true && index > 0) {
                        editTextList[index - 1].requestFocus()
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }

    private fun checkOTP(): Boolean {
        val enteredCode = buildVerificationCode()
        val storedCode = getStoredVerificationCode()

        return enteredCode == storedCode
    }

    private fun buildVerificationCode(): Int {
        val codeBuilder = StringBuilder()
        listOf(binding.tiCode1, binding.tiCode2, binding.tiCode3, binding.tiCode4)
            .forEach { codeBuilder.append(it.text.toString()) }

        return codeBuilder.toString().toIntOrNull() ?: 0
    }

    private fun getStoredVerificationCode(): Int {
        val sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)

        return sharedPreferences.getString("resetCode", "0")?.toIntOrNull() ?: 0
    }
}
