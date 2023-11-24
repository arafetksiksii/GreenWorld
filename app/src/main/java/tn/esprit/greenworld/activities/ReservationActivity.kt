package tn.esprit.greenworld.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.DatePicker
import android.widget.TimePicker
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.greenworld.API.ReservationApi
import tn.esprit.greenworld.adapters.EventAdapter
import tn.esprit.greenworld.databinding.ReservationBinding
import tn.esprit.greenworld.models.Reservation
import tn.esprit.greenworld.utils.RetrofitImp

import java.util.Calendar

class ReservationActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0

    var saveDay = 0
    var saveMonth = 0
    var saveYear = 0
    var saveHour = 0
    var saveMinute = 0
    private lateinit var binding: ReservationBinding
    private lateinit var eventListAdapter: EventAdapter
    private lateinit var userId: String
    private lateinit var eventID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ReservationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Récupérez les données de l'intent
        val eventId = intent.getStringExtra("event_id")
        val eventTitle = intent.getStringExtra("event_titre")
        val eventImage = intent.getStringExtra("event_image")
        val eventDescription = intent.getStringExtra("event_description")
        val eventLocation = intent.getStringExtra("event_location")
        val eventnbParticipant = intent.getStringExtra("event_nbParticpant")?.toIntOrNull() ?: 0
        userId = intent.getStringExtra("user_id") ?: ""

        val eventNameEditText = binding.EventName
        eventNameEditText.setText(eventTitle)

        val userIDEditText = binding.userID
        userIDEditText.setText(userId)

        val eventIDEditText = binding.eventID
        eventIDEditText.setText(eventId)

        binding.calender.setOnClickListener {
            pickDate()

        }
        binding.reservez.setOnClickListener{
            saveReservation()
        }
        binding.fermer.setOnClickListener{
            onBackPressed()

        }
    }

    private fun saveReservation() {
        if (saveYear == 0 || saveMonth == 0 || saveDay == 0 || saveHour == 0 || saveMinute == 0) {
            // La date n'a pas été sélectionnée, afficher un message d'erreur
            showSnackbar("Veuillez sélectionner une date.")
            return
        }
        val cal = Calendar.getInstance()
        cal.set(saveYear, saveMonth, saveDay, saveHour, saveMinute)
        val reservationDate = cal.time

        try {
            val reservation = Reservation(
                date_reservation = reservationDate,
                eventID = binding.eventID.text.toString(),
                userID = binding.userID.text.toString()
            )

            val reservationService = RetrofitImp.buildRetrofit().create(ReservationApi::class.java)

            val call: Call<Reservation> = reservationService.addReservation(reservation)

            call.enqueue(object : Callback<Reservation> {
                override fun onResponse(call: Call<Reservation>, response: Response<Reservation>) {
                    if (response.isSuccessful) {
                        handleSuccessfulRegistration(response.body())
                    } else {
                        handleRegistrationFailure(response.errorBody()?.string().toString())
                    }
                }

                override fun onFailure(call: Call<Reservation>, t: Throwable) {
                    handleRegistrationFailure(t.message.toString())
                }
            })
        } catch (e: Exception) {
            // Handle the date conversion error or any other exceptions
            e.printStackTrace()
            handleRegistrationFailure("Error processing reservation")
        }
    }

    private fun getDateTimeCalendar() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR_OF_DAY)
        minute = cal.get(Calendar.MINUTE)
    }

    private fun pickDate() {
        getDateTimeCalendar()
        DatePickerDialog(this, this, year, month, day).show()
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        saveDay = dayOfMonth
        saveYear = year
        saveMonth = month

        getDateTimeCalendar()
        TimePickerDialog(this, this, hour, minute, true).show()
    }

    override fun onTimeSet(p0: TimePicker?, hourofDay: Int, minute: Int) {
        saveHour = hourofDay
        saveMinute = minute
        binding.dateEvent.text = "$saveDay-$saveMonth-$saveYear\nHour: $saveHour Minute: $saveMinute"
    }

    private fun handleSuccessfulRegistration(events: Reservation?) {
        val customDialog = CustomDialog(this)
        customDialog.show()
    }

    private fun handleRegistrationFailure(errorMessage: String) {
        // Handle registration failure
        showSnackbar(errorMessage)


    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)
}
