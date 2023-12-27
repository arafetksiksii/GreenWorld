
package tn.esprit.greenworld.ui.gestionUser
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.greenworld.R
import tn.esprit.greenworld.databinding.ActivityUserValidationCodeBinding
import tn.esprit.greenworld.models.User
import tn.esprit.greenworld.models.User3
import tn.esprit.greenworld.utils.Login
import tn.esprit.greenworld.utils.RetrofitImp


class User_ValidationCode : AppCompatActivity() {

    private lateinit var binding: ActivityUserValidationCodeBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var countDownTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserValidationCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startCountdownTimer()
        binding.btnVerify.isEnabled = false
        startButtonAnimation()
        binding.btnVerify.setOnClickListener {
            if (checkOTP()) {
                // Verification code is correct
                startActivity(Intent(this, User_RentialiseMotPasse::class.java))
            } else {
                // Verification code is incorrect
                Toast.makeText(this, "Incorrect verification code", Toast.LENGTH_SHORT).show()
                startButtonAnimation()
            }
        }


        sharedPreferences = getSharedPreferences("user_pref", Context.MODE_PRIVATE)

        val userEmail = sharedPreferences.getString("userEmail", "")
        binding.btnResendCode.setOnClickListener {
            RetrofitImp.buildRetrofit().create(Login::class.java).sendResetCode(
                User3(email = userEmail.toString())
            ).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        val user = response.body()
                        Log.d("User_ValidationCode",user.toString())
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
                                this@User_ValidationCode,
                                "User Not Found",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        // Handle the response in case of an error
                        Toast.makeText(
                            this@User_ValidationCode,
                            "Error: ${response.code()} - ${response.errorBody()?.string()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    // Handle the failure case
                    Toast.makeText(
                        this@User_ValidationCode,
                        "Network Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
        binding.btnReturn.setOnClickListener {
            finish()
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

                    // Check if the code is valid and update the button state
                    if (checkOTP()) {
                        stopButtonAnimation()
                        binding.btnVerify.isEnabled = true
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


    private fun startButtonAnimation() {
        Log.d("Animation", "Starting Button Animation")
        binding.btnVerify.apply {
            isEnabled = false
            val animation = AnimationUtils.loadAnimation(context, R.anim.rotate_button)
            startAnimation(animation)
        }
    }




    private fun stopButtonAnimation() {
        Log.d("Animation", "Stopping Button Animation")
        binding.btnVerify.apply {
            clearAnimation()
            isEnabled = true
        }
    }

    private fun startCountdownTimer() {
        countDownTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                binding.txtCountdownTimer.text = getString(R.string.seconds_remaining, secondsRemaining)
            }

            override fun onFinish() {
                binding.txtCountdownTimer.text = getString(R.string.time_up1)
                // Perform any action you need when the timer finishes
            }
        }.start()
    }
    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()  // Avoid memory leaks
    }
}
