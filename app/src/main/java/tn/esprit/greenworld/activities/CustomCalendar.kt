package tn.esprit.greenworld.activities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.applandeo.materialcalendarview.CalendarView
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import android.app.AlertDialog
import android.widget.Button
import android.widget.ImageView
import tn.esprit.greenworld.API.ReservationApi
import tn.esprit.greenworld.R
import tn.esprit.greenworld.models.Event
import tn.esprit.greenworld.models.Reservation
import tn.esprit.greenworld.utils.RetrofitImp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CustomCalendar : AppCompatActivity() {
    private lateinit var reservationApi: ReservationApi
    private lateinit var event: Event
    private lateinit var reservations: List<Reservation> // Ajout de la déclaration
    private var isApiCallCompleted = false // Add a flag

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_calendar)
        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        val btncancel = findViewById<ImageView>(R.id.fermer)

        reservationApi = RetrofitImp.createReservationApi()

        reservationApi.getAllReservations().enqueue(object : Callback<List<Reservation>> {
            override fun onResponse(
                call: Call<List<Reservation>>,
                response: Response<List<Reservation>>
            ) {
                if (response.isSuccessful) {
                    reservations = response.body() ?: emptyList() // Initialisez la propriété ici
                    isApiCallCompleted = true // Set the flag to true

                    val datesList = mutableListOf<EventDay>()

                    reservations?.let {
                        for (reservation in it) {
                            // Afficher les informations de réservation dans la console
                            Log.d(
                                "CustomCalendar",
                                "Date de réservation : ${reservation.date_reservation}"
                            )
                            Log.d("CustomCalendar", "ID de l'événement : ${reservation.eventID}")
                            Log.d("CustomCalendar", "ID de l'utilisateur : ${reservation.userID}")

                            val reservationDate = reservation.date_reservation
                            val calendar = Calendar.getInstance()
                            calendar.time = reservationDate
                            datesList.add(EventDay(calendar, R.drawable.circle_background))
                        }
                        calendarView.setEvents(datesList)


                    }

                }
            }

            override fun onFailure(call: Call<List<Reservation>>, t: Throwable) {
                // Gérez l'échec de la récupération des réservations ici
                Toast.makeText(
                    this@CustomCalendar,
                    "Erreur lors de la récupération des réservations",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        btncancel.setOnClickListener {
            onBackPressed()
        }
        calendarView.setOnDayClickListener(object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {
                // Appelé lorsque l'utilisateur clique sur une date
                showEventDetailsDialog(eventDay.calendar)
            }
        })

    }

    private fun showEventDetailsDialog(selectedDate: Calendar) {

        val selectedDateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate.time)

        val matchingReservations = reservations.filter { reservation ->
            val reservationDateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(reservation.date_reservation)
            reservationDateString == selectedDateString
        }

        if (matchingReservations.isNotEmpty()) {
            val detailsDialog = AlertDialog.Builder(this)
                .setMessage("événement réservé")
                // Ajoutez d'autres détails de l'événement réservé au dialogue
                .setPositiveButton("Fermer", null)
                .create()

            detailsDialog.show()
        } else {
            Toast.makeText(this, "Aucune réservation pour cette date.", Toast.LENGTH_SHORT).show()
        }
    }

}
