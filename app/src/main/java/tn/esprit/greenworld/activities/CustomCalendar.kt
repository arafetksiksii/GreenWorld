package tn.esprit.greenworld.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CalendarView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.greenworld.API.ReservationApi
import tn.esprit.greenworld.R
import tn.esprit.greenworld.models.Reservation
import tn.esprit.greenworld.utils.RetrofitImp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CustomCalendar : AppCompatActivity() {
    private lateinit var reservationApi: ReservationApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_calendar)
        val calendarView = findViewById<CalendarView>(R.id.calenderView)
        reservationApi = RetrofitImp.createReservationApi()
        reservationApi.getAllReservations().enqueue(object : Callback<List<Reservation>> {
            override fun onResponse(
                call: Call<List<Reservation>>,
                response: Response<List<Reservation>>
            ) {
                if (response.isSuccessful) {
                    val reservations = response.body()
                    reservations?.let {
                        val calendarView = findViewById<CalendarView>(R.id.calenderView)

                        // Parcourir les réservations et définir les dates réservées dans le calendrier
                        for (reservation in reservations) {
                            val reservationDate = reservation.date_reservation

                            // Replace non-standard characters in the date string
                            val cleanReservationDate = reservationDate.replace("’", "'")

                            // Utilize SimpleDateFormat to parse the cleaned date string
                            val dateFormat =
                                SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z (zzzz)", Locale.getDefault())
                            val parsedDate = dateFormat.parse(cleanReservationDate)

                            // Utilize Calendar to extract the year, month, and day from the reservation date
                            val calendar = Calendar.getInstance()
                            parsedDate?.let {
                                calendar.time = it
                                val year = calendar.get(Calendar.YEAR)
                                val month = calendar.get(Calendar.MONTH)
                                val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

                                // Set the reserved date in the CalendarView
                                calendarView.setDate(calendar.timeInMillis, true, true)
                            }
                        }
                    }
                }
            }
            override fun onFailure(call: Call<List<Reservation>>, t: Throwable) {
                Log.d("CustomCalendar", "Erreur lors de la récupération des réservations", t)
            }
        })
    }
}