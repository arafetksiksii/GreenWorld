package com.example.testtt.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.testtt.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class dechets : Fragment() {

    private lateinit var selectedDateTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dechets, container, false)
        val button = view.findViewById<ImageButton>(R.id.btndate)
        selectedDateTextView = view.findViewById(R.id.textViewSelectedDate)

        button.setOnClickListener {
            openDatePicker()
           // openTimePicker()
        }

        return view
    }
    private fun openDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)

                val timePickerDialog =
                    TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
                        val selectedTime = Calendar.getInstance()
                        selectedTime.set(Calendar.HOUR_OF_DAY, selectedHour)
                        selectedTime.set(Calendar.MINUTE, selectedMinute)

                        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

                        val formattedDate = dateFormat.format(selectedDate.time)
                        val formattedTime = timeFormat.format(selectedTime.time)

                        // Concatenate date and time
                        val dateTimeString = "$formattedDate $formattedTime"

                        // Update the TextView with the concatenated date and time
                        selectedDateTextView.text = dateTimeString
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)

                timePickerDialog.show()
            }, year, month, day)

        datePickerDialog.show()
    }




}