package tn.esprit.greenworld.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.ImageButton

import androidx.appcompat.app.AppCompatActivity


import com.google.android.gms.maps.GoogleMap
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.greenworld.API.DechetsApi
import tn.esprit.greenworld.R
import tn.esprit.greenworld.databinding.ActivityMaindechetsBinding
import tn.esprit.greenworld.models.Dechets
import tn.esprit.greenworld.models.DechetsItem
import tn.esprit.greenworld.models.User
import tn.esprit.greenworld.utils.RetrofitImp
import tn.esprit.greenworld.utils.UserInterface
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class Maindechets : AppCompatActivity() {
    private lateinit var binding: ActivityMaindechetsBinding
    private lateinit var map: GoogleMap
    private lateinit var TypeTitre: String
    private val PREF_NAME = Constants.PREF_NAME
    private val USER_ID_KEY = Constants.USER_ID_KEY


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMaindechetsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val rootView = window.decorView.rootView
        initTextWatchers()
        val TypeTitle = intent.getStringExtra("type_titre")

        val eventNameEditText = binding.titype
        eventNameEditText.setText(TypeTitle)
        Log.d("typeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee", TypeTitle.toString())



        val button = findViewById<ImageButton>(R.id.btndate)
        binding.btnSend.setOnClickListener {

            if (validateForm()) {

                registerDechets()
                onClick()

            } else {
                Snackbar.make(rootView, getString(R.string.msg_error_inputs), Snackbar.LENGTH_SHORT)
                    .show()
            }
        }


        button.setOnClickListener {
            openDatePicker()
        }

        binding.btnReturn.setOnClickListener {
            finish()
        }
        binding.buttonShowOnMap.setOnClickListener {
            val locationName = binding.tiAdresse.text.toString()

            val geocoder = Geocoder(this)
            try {
                val addressList: List<Address>? = geocoder.getFromLocationName(locationName, 1)
                if (addressList != null && addressList.isNotEmpty()) {
                    val address: Address = addressList[0]
                    val latitude: Double = address.latitude
                    val longitude: Double = address.longitude

                    val intent = Intent(this, MapsActivity::class.java)
                    intent.putExtra("LATITUDE", latitude)
                    intent.putExtra("LONGITUDE", longitude)
                    startActivity(intent)
                } else {
                    // Adresse non trouvée
                    // Gérez cela en conséquence
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }






    }




    //MMMAAAPPP
    private fun onClick() {
        val qrImageView = binding.idIVQrcode
        val type = binding.titype.text.toString()
        val date = binding.selectedDateTextView.text.toString()
        val weight = binding.tiWeight.text.toString()
        val adresse = binding.tiAdresse.text.toString()

        // Concaténer les différentes données en une seule chaîne
        val content = "$type\n$date\n$weight\n$adresse"

        val multiFormatWriter = MultiFormatWriter()

        try {
            val bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, 500, 500)
            val barcodeEncoder = BarcodeEncoder()
            val bitmap: Bitmap = barcodeEncoder.createBitmap(bitMatrix)
            qrImageView.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }



    private fun initTextWatchers() {


        binding.titype.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateType()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.selectedDateTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateDate()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        binding.tiWeight.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateWeight()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.tiAdresse.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun validateDate(): Boolean {
        val dateDepot = binding.selectedDateTextView.text.toString()
        binding.tiDateLayout.error = null

        if (dateDepot.isEmpty()) {
            binding.tiDateLayout.error = getString(R.string.msg_must_not_be_empty)
            return false
        }

        return true
    }

    private fun validateWeight(): Boolean {
        val nombreCapacite = binding.tiWeight.text.toString()
        binding.tiWeightLayout.error = null

        if (nombreCapacite.isEmpty()) {
            binding.tiWeightLayout.error = getString(R.string.msg_must_not_be_empty)
            return false
        }

        if (nombreCapacite.length < 3) {
            binding.tiWeightLayout.error = getString(R.string.msg_check_your_characters)
            return false
        }

        return true
    }



    private fun validateType(): Boolean {


        val type = binding.titype.text.toString()
        binding.tiTypeLayout.error = null

        if (type.isEmpty()) {
            binding.tiTypeLayout.error = getString(R.string.msg_must_not_be_empty)
            return false
        }

        return true

    }

    private fun validateForm(): Boolean {
        return validateType()&& validateDate() && validateWeight()
    }

    private fun openDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)

                val timePickerDialog = TimePickerDialog(
                    this,
                    { _, selectedHour, selectedMinute ->
                        val selectedTime = Calendar.getInstance()
                        selectedTime.set(Calendar.HOUR_OF_DAY, selectedHour)
                        selectedTime.set(Calendar.MINUTE, selectedMinute)

                        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

                        val dateString =
                            dateFormat.format(selectedDate.time) + " " + timeFormat.format(
                                selectedTime.time
                            )
                        try {
                            binding.selectedDateTextView.text = dateString.toEditable()
                        } catch (e: Exception) {
                            // Handle the date conversion error
                        }
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                )

                timePickerDialog.show()
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }




    private fun registerDechets() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val userId = getUserIdFromPreferences() ?: "defaultUserID"

        try {
            val date = dateFormat.parse(binding.selectedDateTextView.text.toString())

            val dechets = DechetsItem(
               Type_dechets = binding.titype.text.toString(),
                date_depot = binding.selectedDateTextView.text.toString(),
                nombre_capacite = binding.tiWeight.text.toString().toIntOrNull() ?: 0,
                adresse = binding.tiAdresse.text.toString(),
                userID = userId,
                )

            val registerService = RetrofitImp.buildRetrofit().create(DechetsApi::class.java)


            registerService.addDechets(dechets).enqueue(object : Callback<Dechets> {
                override fun onResponse(call: Call<Dechets>, response: Response<Dechets>) {
                    if (response.isSuccessful) {
                        // Handle successful registration
                        updatePointsForCurrentUser()
                        handleSuccessfulRegistration(response.body())
                    } else {
                        // Handle registration failure
                        handleRegistrationFailure(response.errorBody()?.string().toString())
                    }
                }

                override fun onFailure(call: Call<Dechets>, t: Throwable) {
                    // Handle registration failure
                    handleRegistrationFailure(t.message.toString())
                }
            })
        } catch (e: Exception) {
            // Handle exceptions
            e.printStackTrace()
            // Handle registration failure in case of an exception
            handleRegistrationFailure("Error occurred during registration")
        }
    }

    private fun updatePointsForCurrentUser() {
        val userId = getUserIdFromPreferences()
        if (userId != null) {
            val userService = RetrofitImp.buildRetrofit().create(UserInterface::class.java)
            userService.updateUserPoints(userId).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        // Points mis à jour avec succès
                    } else {
                        // Échec de la mise à jour des points
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    // Échec de la mise à jour des points
                }
            })
        }
    }

    private fun getUserIdFromPreferences(): String? {
        val sharedPreferences: SharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(USER_ID_KEY, null)

    }

    private fun handleSuccessfulRegistration(dechets: Dechets?) {
        startActivity(Intent(this@Maindechets, valider_dechets::class.java))


        val qrImageView = binding.idIVQrcode
        val type = binding.titype.text.toString()
        val date = binding.selectedDateTextView.text.toString()
        val weight = binding.tiWeight.text.toString()
        val adresse = binding.tiAdresse.text.toString()

// Concaténer les différentes données en une seule chaîne
        val content = "$type\n$date\n$weight\n$adresse"

        val multiFormatWriter = MultiFormatWriter()
        lateinit var bitmap: Bitmap // Déclaration de la variable bitmap

        try {
            val bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, 500, 500)
            val barcodeEncoder = BarcodeEncoder()
            bitmap = barcodeEncoder.createBitmap(bitMatrix)
            qrImageView.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            e.printStackTrace()
        }

        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()

        val intent = Intent(this@Maindechets, valider_dechets::class.java)
        intent.putExtra("Type_dechets", type)
        intent.putExtra("date_depot", date)
        intent.putExtra("nombre_capacite", weight)
        intent.putExtra("adresse", adresse)
        intent.putExtra("qrcode", byteArray)

        startActivity(intent)


    }


    private fun handleRegistrationFailure(errorMessage: String) {
        // Handle registration failure
        showSnackbar(errorMessage)
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}

fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this?: "")